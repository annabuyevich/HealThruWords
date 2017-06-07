package app.com.example.annab.quotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.com.example.annab.quotes.R;
import app.com.example.annab.quotes.authenticator.Authenticate;

/**
 * LoginActivity:
 * The first activity that gets run. Every preceding activity extends this activity.
 * Contains necessary methods to set up the FirebaseAuthentication.
 *
 * @author anna
 */

public class LoginActivity extends AppCompatActivity {

    // get the firebase database and the authentication
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private static boolean loginTracker = false;

    private Authenticate authenticate = new Authenticate();
    private static boolean signedIn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // initialize the mAuthStateListener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            /**
             * Adds an oberver for auth state changes.
             * @param firebaseAuth contains information of whether the user at that
             *                     moment is authenticated or not.
             */
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // check if the user is logged in or not
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // user signed in
                    Log.d("SIGNED IN", "logged in");
                    signedIn = true;

                    if (!loginTracker) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        loginTracker = true;
                    }

                } else {
                    Log.d("SIGNED OUT", "logged out");
                    signedIn = false;
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setTheme(R.style.FullscreenTheme)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
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

        // if the activity that was returned was our login flow then we will do this
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed out.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * Creates an inflator with information from the main_menu xml
     *
     * @param menu an attribute the onCreate can contain
     * @return true after all the information is inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Creates a reaction when the menu button is clicked
     *
     * @param item menu items that are clicked
     * @return true when user signed out or the super result of this method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                // clicked sign out
                AuthUI.getInstance().signOut(this);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Removes the authentication state listener, cleans up listener, and makes sure the activity
     * is destroyed in a way that has nothing to do with signing out.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // removing the authentication state listener
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

        authenticate.detachDatabaseReadListener();
    }

    /**
     * Adds the authenticate state listener.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public boolean getSignedIn() {
        return signedIn;
    }
}
