package app.com.example.annab.quotes.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import app.com.example.annab.quotes.R;
import app.com.example.annab.quotes.helpers.SendMailBySite;
import app.com.example.annab.quotes.pojos.Quote;


/**
 * QuoteDetailActivity:
 * Looks at a specific quote.
 *
 * @author anna
 */

public class QuoteDetailActivity extends LoginActivity {

    private ImageView mQuoteImageView;
    private ImageButton mChatButton;
    private ImageButton mEmailButton;
    private TextView mTitleView;
    private com.like.LikeButton mLikeButton;

    private FirebaseAuth mFirebaseAuth;
    private static String userEmail;
    private Quote quote;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_detail);

        mFirebaseAuth = FirebaseAuth.getInstance();

        initializeViews();

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("QUOTE");
        bundle.setClassLoader(Quote.class.getClassLoader());

        quote = bundle.getParcelable("quote");

        Picasso.with(mQuoteImageView.getContext()).load(quote.getMedia()).into(mQuoteImageView);
        mTitleView.setText(quote.getTitle().toUpperCase());

        // send email when clicked
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                assert user != null;
                userEmail = user.getEmail();
                Log.d("Clicked Email", userEmail);
                new EmailAsyncTask().execute();

                Toast.makeText(getApplicationContext(), "Quote link was sent to your email.", Toast.LENGTH_LONG).show();
            }
        });

        mLikeButton.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setEnabled(true);
                new ImageAsyncTask(getApplicationContext()).execute(quote.getMedia());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setEnabled(false);
                likeButton.setSaveEnabled(false);
                new ImageAsyncTask(getApplicationContext()).execute(quote.getMedia());
            }
        });
    }

    /**
     * Initializes the views in quote detail xml.
     */
    private void initializeViews() {
        mQuoteImageView = (ImageView) findViewById(R.id.quoteImageView);
        mChatButton = (ImageButton) findViewById(R.id.chatButton);
        mEmailButton = (ImageButton) findViewById(R.id.mailButton);
        mTitleView = (TextView) findViewById(R.id.titleView);
        mLikeButton = (LikeButton) findViewById(R.id.like_button);
    }

    /**
     * When the logo is clicked goes to the MainActivity is called.
     *
     * @param v the view of the current activity
     */
    public void goToHomeScreen(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * When the message imageView is clicked goes to the MessageActivity.
     *
     * @param v the view of the current activity
     */
    public void goToChat(View v) {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }


    /**
     * ImageAsyncTask:
     * Takes String and transforms it into a bitmap and then compresses the bitmap and stores it
     * in a directory.
     *
     * @author annavb2
     */
    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        // requires context so we can make Toasts
        private Context context;
        private Bitmap bitmap = null;
        private String filename = quote.getTitle();


        ImageAsyncTask(Context context) {
            this.context = context;
        }

        /**
         * Takes the quoteimage url string and transforms it in to a bitmap
         *
         * @param params the quoteImage
         * @return the bitmap of the quoteImage
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                bitmap = Glide.with(context)
                        .load(params[0])
                        .asBitmap()
                        .into(-1, -1)
                        .get();
                return bitmap;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;

        }

        /**
         * Creates a file and directory to store the quote images
         *
         * @param mBitmap the quoteimage that was just turned into a bitmap
         */
        @Override
        protected void onPostExecute(Bitmap mBitmap) {

            File originalFile = new File(Environment.getExternalStorageDirectory() + "/Quotes/");
            if (!originalFile.exists()) {
                originalFile.mkdirs();
                Toast.makeText(getApplicationContext(), "New Folder Created", Toast.LENGTH_SHORT)
                        .show();
            }
            OutputStream outStream;

            File file = new File(Environment.getExternalStorageDirectory() +
                    "/Quotes/" + filename + ".png");
            try {
                if (mLikeButton.isSaveEnabled()) {
                    outStream = new FileOutputStream(file);
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
                    outStream.close();
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                } else {
                    boolean deleted = file.delete();
                    if (deleted) {
                        Toast.makeText(getApplicationContext(), "Unsaved", Toast.LENGTH_LONG)
                                .show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Sends an email while the app is running.
     *
     * @author annavb2
     */
    private class EmailAsyncTask extends AsyncTask<Void, Void, Void> {

        /**
         * This function is done on the background thread.
         *
         * @param params void
         * @return null
         */
        @Override
        protected Void doInBackground(Void... params) {
            SendMailBySite.sendEmail(userEmail, quote.getMedia());
            return null;
        }
    }
}