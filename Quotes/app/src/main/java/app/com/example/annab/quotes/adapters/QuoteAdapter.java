package app.com.example.annab.quotes.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.example.annab.quotes.R;
import app.com.example.annab.quotes.activities.QuoteDetailActivity;
import app.com.example.annab.quotes.pojos.Quote;

/**
 * QuoteAdapter:
 * Fills the RecyclerView with the Quote data that was loaded.
 *
 * @author anna
 */
public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {

    private static final String QUOTE = "QUOTE";
    private List<Quote> quotes;
    private ArrayList<Quote> uniqueQuoteList = new ArrayList<>();
    private HashMap<Integer, Quote> uniqueQuoteMap = new HashMap<>();

    public QuoteAdapter(List<Quote> quotes) {
        this.quotes = quotes;
    }

    /**
     * This function is called just until the whole screens is filled with views. The views are
     * recycled once the scrolling begins.
     *
     * @param parent   the RecyclerView which is the intended parent
     * @param viewType allows different types of views in the same RecyclerView
     * @return a new ViewHolder we assigned
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // the LayoutInflater turns the layout in the XML to a View object.
        final View quoteListItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.quote_list_item, parent, false);
        return new ViewHolder(quoteListItem);
    }

    /**
     * This functions is called when a ViewHolder needs to hold data from a different position in
     * the list. Since we are using recycling we don't have to update the content in the views.
     * When the view holder is clicked an explicit intent is launched to another activity that
     * contains more information about a quote.
     *
     * @param holder   the ViewHolder that knows about which Views we need to update
     * @param position the index used for the list of quotes
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // access a quote
        final Quote quote = uniqueQuoteList.get(position);

        Picasso.with(holder.imageView.getContext()).load(quote.getMedia()).into(holder.imageView);

        // attach click listener that launches a new Activity, Detail Activity
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launches an explicit intent to to another activity in the same app
                Intent intent = new Intent(v.getContext(), QuoteDetailActivity.class);

                // pass the data the quote contains to a as a parcelable, plain old java object
                Bundle bundle = new Bundle();
                bundle.putParcelable("quote", quote);
                intent.putExtra(QUOTE, bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * The number of quotes in the list of quotes. This is necessary because the RecyclerView want
     * know when to stop scrolling, which is when it gets to the end of the list.
     *
     * @return the number of quote in the list of quote
     */
    @Override
    public int getItemCount() {
        return uniqueQuoteList.size();
    }

    /**
     * ViewHolder:
     * Stores the references to the subview so we don't have to look them up each time.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.quoteImageView);
        }
    }

    /**
     * Checks whether the random url is not one that was previously added to the list of quotes and
     * makes sure that the quote url is valid.
     */
    public void cleanUpQuotes() {

        for (Quote quote : quotes) {
            if (!uniqueQuoteMap.containsKey(quote.getId()) && (quote.getMedia().contains(".png")
                    || quote.getMedia().contains(".jpg"))) {
                uniqueQuoteMap.put(quote.getId(), quote);
                uniqueQuoteList.add(quote);
            }
        }
    }
}




