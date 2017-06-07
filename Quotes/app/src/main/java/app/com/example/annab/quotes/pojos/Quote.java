package app.com.example.annab.quotes.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Quote:
 * Plane Java Object that implements Parcelable and stores the information from the API.
 *
 * @author anna
 */
public class Quote implements Parcelable {
    private int id;
    private String title;
    private String author;
    private String url;
    private String cat;
    private String media;

    public Quote() {
    }

    public Quote(int id, String media) {
        this.id = id;
        this.media = media;
    }

    private Quote(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        url = in.readString();
        cat = in.readString();
        media = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(url);
        dest.writeString(cat);
        dest.writeString(media);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public String getCat() {
        return cat;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getMedia() {
        return media;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
