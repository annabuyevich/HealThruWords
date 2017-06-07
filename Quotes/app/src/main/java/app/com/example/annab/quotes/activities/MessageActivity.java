package app.com.example.annab.quotes.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import app.com.example.annab.quotes.authenticator.Authenticate;
import app.com.example.annab.quotes.adapters.MessageAdapter;
import app.com.example.annab.quotes.pojos.QuoteMessage;
import app.com.example.annab.quotes.R;

/**
 * MessageActivity:
 * This activity displays the message chat.
 * <p>
 * Implemented code from:
 * https://www.udacity.com/course/firebase-in-a-weekend-by-google-android--ud0352
 *
 * @author anna
 */

public class MessageActivity extends LoginActivity {

    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER = 2;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private Authenticate authenticate = new Authenticate();
    private DatabaseReference mMessagesDatabaseReference;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mQuotePhotoStorageReference;
    private FirebaseAuth mFirebaseAuth;

    private NotificationCompat.Builder mBuilder;
    private Context context;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail);

        initializeViews();

        getFirebaseRoot();

        // intialize the message List view and it adapter
        List<QuoteMessage> quoteMessage = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, quoteMessage);
        mMessageListView.setAdapter(mMessageAdapter);


        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        assert user != null;
        authenticate.onSignedInInitialize(user.getDisplayName(), mMessageAdapter);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                        RC_PHOTO_PICKER);

            }
        });

        // send button when there is text to send, textwatcher makes it so that we can't send
        // empty messages
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // the button won't send if the text is empty
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter
                (DEFAULT_MSG_LENGTH_LIMIT)});


        // send button message and clear the edit text
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // quoteMessage contains all the keys that will be stored in the real time database
                QuoteMessage quoteMessage = new QuoteMessage(mMessageEditText.getText().toString(),
                        authenticate.getmUsername(), null);
                mMessagesDatabaseReference.push().setValue(quoteMessage);

                sendNotification(quoteMessage);

                // clear input
                mMessageEditText.setText("");
            }
        });
    }

    /**
     * Gets root node and looks at the messaging portion of the database.
     */
    private void getFirebaseRoot() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mQuotePhotoStorageReference = mFirebaseStorage.getReference().child("quote_photos");
    }

    /**
     * Initialize all the views for the message activity layout.
     */
    private void initializeViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
    }

    /**
     * Sends android notification when user sends message.
     *
     * @param quoteMessage the pojo that contains the quoteMessage information
     */
    private void sendNotification(QuoteMessage quoteMessage) {
        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);


        // notification's filled with custom notification layout
        remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.notif_icon, R.mipmap.logo);
        remoteViews.setTextViewText(R.id.notif_title, quoteMessage.getText());
        remoteViews.setTextViewText(R.id.userTextView, quoteMessage.getName() + " : ");
        remoteViews.setImageViewResource(R.id.personImageView,
                R.drawable.com_facebook_profile_picture_blank_square);

        notification_id = (int) System.currentTimeMillis();

        Intent button_intent = new Intent("button_click");
        button_intent.putExtra("id", notification_id);
        PendingIntent button_pending_event = PendingIntent.getBroadcast(context, notification_id,
                button_intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.button, button_pending_event);

        Intent notification_intent = new Intent(context, MessageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notification_intent, 0);

        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notification_id, mBuilder.build());
    }

    /**
     * When the user is done with the activity this method is called.
     *
     * @param requestCode the number passed into startActivityResult()
     * @param resultCode  contains the information whether the operation was a success or the
     *                    user backed out or the operation failed
     * @param data        the intent that contains the result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // take reference from quote photos and making a child,
            // the file name stored is the last thing in the url name
            StorageReference photoReference = mQuotePhotoStorageReference
                    .child(selectedImageUri.getLastPathSegment());

            // upload the file to the firebase storage, done sending the photo to storage
            photoReference.putFile(selectedImageUri).addOnSuccessListener
                    (this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        // key getting the url

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            assert downloadUrl != null;
                            QuoteMessage quoteMessage = new QuoteMessage(null,
                                    authenticate.getmUsername(), downloadUrl.toString());
                            mMessagesDatabaseReference.push().setValue(quoteMessage);
                        }
                    });
        }
    }
}
