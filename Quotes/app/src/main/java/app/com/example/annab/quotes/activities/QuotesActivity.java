package app.com.example.annab.quotes.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import app.com.example.annab.quotes.Api;
import app.com.example.annab.quotes.R;
import app.com.example.annab.quotes.adapters.QuoteAdapter;
import app.com.example.annab.quotes.helpers.RequestQuotes;
import app.com.example.annab.quotes.pojos.Quote;

/**
 * QuotesActivity:
 * Displays a collection of quotes.
 *
 * @author anna
 */
public class QuotesActivity extends LoginActivity {

    private List<Quote> mQuotes = new ArrayList<>();
    private QuoteAdapter mQuoteAdapter;
    private ArrayList<Quote> quoteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_activity);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerQuotes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));

        mQuoteAdapter = new QuoteAdapter(mQuotes);
        mRecyclerView.setAdapter(mQuoteAdapter);
        constructData();
    }

    /**
     * Access the mashape api, constructs the urls, and requests the data.
     */
    private void constructData() {

        final String URL_STRING = "https://healthruwords.p.mashape.com/v1/quotes/?maxR=";

        int NUM_QUOTES_COLLECTED = 5;
        int MULTIPLE_OF_QUOTES = 4;
        URL[] urls = new URL[NUM_QUOTES_COLLECTED];

        try {
            // gets 5 quotes every time the loop is iterated
            for (int numCollectedQuotes = 0; numCollectedQuotes < MULTIPLE_OF_QUOTES;
                 numCollectedQuotes++) {
                urls[numCollectedQuotes] = new URL(URL_STRING + NUM_QUOTES_COLLECTED +
                        "&size=medium&mashape-key=" + Api.KEY);

                // Fetches the quote on a background thread which will populate mQuotes
                new QuotesActivity.QuoteAsyncTask(this).execute(urls[numCollectedQuotes]);
            }
        } catch (MalformedURLException e) {
            // runs if the URL string is not parsable or contains an unsupported protocol
            e.printStackTrace();
        }
    }

    /**
     * QuoteAsyncTask:
     * Takes URLS, makes an HTTP request, parses the JSON using GSON into a Quote[],
     * and returns an ArrayList of quotes
     *
     * @author annavb2
     */
    private class QuoteAsyncTask extends AsyncTask<URL, Void, ArrayList<Quote>> {

        // requires context so we can make Toasts
        private Context context;

        QuoteAsyncTask(Context context) {
            this.context = context;
        }

        /**
         * This function is done on the background thread.
         *
         * @param urls each URL contains a different page from the database
         * @return an ArrayList<Quote> representing all the quotes found from the urls
         */
        @Override
        protected ArrayList<Quote> doInBackground(URL... urls) {
            RequestQuotes requestQuotes = new RequestQuotes();
            return requestQuotes.getData(urls, quoteList);
        }

        /**
         * This function is run on the UI thread.
         *
         * @param quotes an ArrayList of quotes
         */
        @Override
        protected void onPostExecute(ArrayList<Quote> quotes) {

            // Generate a Toast if we fail to get the data.
            if (quotes == null) {
                Toast.makeText(context, "Failed to get network data", Toast.LENGTH_LONG).show();
                return;
            }

            int quoteNum = 0;

            try {
                for (Quote quote : quotes) {
                    quoteNum++;
                    Log.d("QUOTES", quote.getTitle() + " - " + quoteNum);
                    Log.d("QUOTE_IMAGE", quote.getMedia());
                    Log.d("QUOTE_ID", String.valueOf(quote.getId()));

                    // populate with fresh quote
                    mQuotes.add(quote);
                }
            } catch (ConcurrentModificationException e) {
                // this is caught when another thread is being called and
                // the collection is being modified
                Log.d("THREAD FAIL", String.valueOf(e));
            }

            mQuoteAdapter.cleanUpQuotes();

            // notifies mQuoteAdapter to let it know that its data changed.
            mQuoteAdapter.notifyDataSetChanged();
        }
    }
}


