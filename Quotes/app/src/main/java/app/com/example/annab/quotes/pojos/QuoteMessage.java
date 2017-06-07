package app.com.example.annab.quotes.pojos;

/**
 * QuoteMessage:
 * A Plain Java Object for the Quote Message that contains the elements of the chat.
 *
 * @author anna
 */

public class QuoteMessage {

    private String text;
    private String name;
    private String photoUrl;

    public QuoteMessage() {
    }

    public QuoteMessage(String text, String name, String photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
