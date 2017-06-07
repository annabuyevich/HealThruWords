package app.com.example.annab.quotes.authenticator;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.com.example.annab.quotes.adapters.MessageAdapter;
import app.com.example.annab.quotes.pojos.QuoteMessage;

/**
 * Authenticate:
 * Contains methods that enforce authentication for the user.
 *
 * @author anna
 */

public class Authenticate {

    private String mUsername;
    private ChildEventListener mChildEventListener;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    // getting root node and saying interested in messaging portion of the database
    private DatabaseReference mMessagesDatabaseReference = mFirebaseDatabase.getReference()
            .child("messages");

    public String getmUsername() {
        return mUsername;
    }

    /**
     * Allows the message to be associated with the correct user that is signed in. Also only sets
     * the message when the user is logged in.
     *
     * @param username        the user's username
     * @param mMessageAdapter that contains all the data
     */
    public void onSignedInInitialize(String username, final MessageAdapter mMessageAdapter) {
        // any message that is set will have this username
        mUsername = username;

        // only setting the messages when the user is logged in
        attachDatabaseReadListener(mMessageAdapter);
    }

    /**
     * Creating a ChildEvent Listener if it's null and add the message to the database.
     *
     * @param mMessageAdapter that contains all the data
     */
    private void attachDatabaseReadListener(final MessageAdapter mMessageAdapter) {

        if (mChildEventListener == null) {

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // the get value deserialize the message to the database to the
                    // QuoteMessage object
                    QuoteMessage quoteMessage = dataSnapshot.getValue(QuoteMessage.class);
                    mMessageAdapter.add(quoteMessage);
                }

                // gets called when the content of an existing message get changed
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                // gets called when message was removed
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                // gets called when the message was moved
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                // gets called when there is an error error
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };

            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    /**
     * Makes sure the even listener is not null and removes the evenListener from
     * messageDatabaseReference.
     */
    public void detachDatabaseReadListener() {
        // make sure the event listener is not null
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
