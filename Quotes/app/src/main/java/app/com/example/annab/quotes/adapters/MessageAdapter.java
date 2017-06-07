package app.com.example.annab.quotes.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.com.example.annab.quotes.pojos.QuoteMessage;
import app.com.example.annab.quotes.R;

/**
 * MessageAdapter:
 * Displays the data collected from QuoteMessenger into the chat.
 *
 * @author anna
 */

public class MessageAdapter extends ArrayAdapter<QuoteMessage> {
    public MessageAdapter(Context context, int resource, List<QuoteMessage> objects) {
        super(context, resource, objects);
    }

    /**
     * Displays the data at the specified position in the database.
     *
     * @param position    the specified position of the item within the adapter's data set
     * @param convertView the old view that is reused if it's not null otherwise a new view
     *                    is created
     * @param parent      the intended parent that this view will be attached to
     * @return a view corresponding to the data at the position
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().
                    inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        QuoteMessage message = getItem(position);

        // check if method has photo url and displays an imageview rather than a textview
        assert message != null;
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);

        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }

        // no matter if the message is a an image or text the author is assigned
        authorTextView.setText(message.getName());

        return convertView;
    }
}
