package app.com.example.annab.quotes.helpers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * ButtonListener:
 * Deals with android notification.
 *
 * @author anna
 */

public class ButtonListener extends BroadcastReceiver {

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent brpadcast.
     *
     * @param context the context in which the receiver is running.
     * @param intent  the intent being recieved.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(intent.getExtras().getInt("id"));
        Toast.makeText(context, "Button Clicked!", Toast.LENGTH_SHORT).show();
    }
}
