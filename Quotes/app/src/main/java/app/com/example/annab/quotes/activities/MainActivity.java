package app.com.example.annab.quotes.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import app.com.example.annab.quotes.R;
import app.com.example.annab.quotes.services.ChatHeadService;


/**
 * MainActivity:
 * First Activity that is run after the user is logged in.
 *
 * @author anna
 */
public class MainActivity extends LoginActivity {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else if (getSignedIn()) {
            initializeView();
        }

    }

    /**
     * Set and initialize the view elements. Deals with the chat icon that is popped up.
     */
    private void initializeView() {
        findViewById(R.id.notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, ChatHeadService.class));
                Intent intent = new Intent(getApplicationContext(), QuotesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Dealing with the email notification.
     *
     * @param requestCode the number passed into startActivityResult()
     * @param resultCode  contains the information whether the operation was a success or the
     *                    user backed out or the operation failed
     * @param data        the intent that contains the result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * When the continue button is clicked, the QuoteActivity is called.
     *
     * @param v the view of the activity.
     */
    public void goToQuotes(View v) {
        Intent intent = new Intent(this, QuotesActivity.class);
        startActivity(intent);
    }
}




