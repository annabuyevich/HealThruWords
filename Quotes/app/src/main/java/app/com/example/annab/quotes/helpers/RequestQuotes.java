package app.com.example.annab.quotes.helpers;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

import app.com.example.annab.quotes.pojos.Quote;

/**
 * RequestQuotes:
 * Contains a method to get an ArrayList of quotes.
 *
 * @author anna
 */

public class RequestQuotes {

    public ArrayList<Quote> getData(URL[] urls, ArrayList<Quote> quoteList) {
        try {
            // runs through the urls and adds the quotes from all the urls to quoteList
            for (URL url : urls) {
                URLConnection connection = url.openConnection();
                connection.connect();

                InputStream inStream = connection.getInputStream();
                InputStreamReader inStreamReader = new InputStreamReader(inStream,
                        Charset.forName("UTF-8"));

                Gson gson = new Gson();
                Quote[] quotes = gson.fromJson(inStreamReader,
                        Quote[].class);

                // adds all the elements in quotes to quoteList
                if (quotes != null) {
                    Collections.addAll(quoteList, quotes);
                }

            }
            return quoteList;

        } catch (UnknownHostException e) {
            // catches an improper internet connection
            Log.d("QuoteAsyncTask", "The IP address could not be determined. " +
                    "Check your internet connection.", e);
            return null;
        } catch (IOException e) {
            Log.d("QuoteAsyncTask", "failed to get data from network", e);
            return null;
        }
    }
}
