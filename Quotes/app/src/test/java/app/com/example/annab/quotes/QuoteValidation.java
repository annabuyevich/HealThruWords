package app.com.example.annab.quotes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import app.com.example.annab.quotes.pojos.Quote;

import static junit.framework.Assert.assertTrue;

/**
 * QuoteValidation:
 * Tests weather no duplicates or bad urls are added to the uniqueQuoteList
 *
 * @author anna
 */

public class QuoteValidation {

    /**
     * The method used to validate the quote that is displayed in the QuoteDetailActivity
     *
     * @param quotes          the arraylist with all the quotes
     * @param uniqueQuoteMap  stores the unique quotes
     * @param uniqueQuoteList contains all the valid quotes
     */
    private void quoteValidator(ArrayList<Quote> quotes, HashMap<Integer, Quote> uniqueQuoteMap, ArrayList<Quote> uniqueQuoteList) {
        for (Quote quote : quotes) {
            if (!uniqueQuoteMap.containsKey(quote.getId()) && (quote.getMedia().contains(".png")
                    || quote.getMedia().contains(".jpg"))) {
                uniqueQuoteMap.put(quote.getId(), quote);
                uniqueQuoteList.add(quote);
            }
        }
    }

    @Test
    public void testAllValidQuotes() {
        ArrayList<Quote> quotes = new ArrayList<>();
        HashMap<Integer, Quote> uniqueQuoteMap = new HashMap<>();
        ArrayList<Quote> uniqueQuoteList = new ArrayList<>();

        Quote quoteOne = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Healthruwords.com_-_Inspirational_Images_-_From-Thoughts-to-Things-300x300.jpg");
        Quote quoteTwo = new Quote(545, "http://healthruwords.com/wp-content/uploads/2014/10/inspirational-pictures-Eyes-of-God-300x291.jpg");
        Quote quoteThree = new Quote(1115, "http://healthruwords.com/wp-content/uploads/2014/10/Sep-8-Let-s-Make-Life-Less-Difficult-300x300.jpg");
        Quote quoteFour = new Quote(12484, "http://healthruwords.com/wp-content/uploads/2017/03/Healthruwords.com_-_Inspirational_Images_-_Super-Human-300x300.png");
        Quote quoteFive = new Quote(12420, "http://healthruwords.com/wp-content/uploads/2017/02/Healthruwords.com_-_Inspirational_Images_-_Beliefs-and-Life-Alignment-300x300.png");

        quotes.add(quoteOne);
        quotes.add(quoteTwo);
        quotes.add(quoteThree);
        quotes.add(quoteFour);
        quotes.add(quoteFive);

        ArrayList<Quote> expectedList = new ArrayList<>();
        expectedList.add(quoteOne);
        expectedList.add(quoteTwo);
        expectedList.add(quoteThree);
        expectedList.add(quoteFour);
        expectedList.add(quoteFive);

        quoteValidator(quotes, uniqueQuoteMap, uniqueQuoteList);

        assertTrue(expectedList.equals(uniqueQuoteList));
    }


    @Test
    public void testDuplicateId() {
        ArrayList<Quote> quotes = new ArrayList<>();
        HashMap<Integer, Quote> uniqueQuoteMap = new HashMap<>();
        ArrayList<Quote> uniqueQuoteList = new ArrayList<>();

        Quote quoteOne = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Healthruwords.com_-_Inspirational_Images_-_From-Thoughts-to-Things-300x300.jpg");
        Quote quoteTwo = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Healthruwords.com_-_Inspirational_Images_-_From-Thoughts-to-Things-300x300.jpg");

        ArrayList<Quote> expectedList = new ArrayList<>();
        expectedList.add(quoteOne);

        quotes.add(quoteOne);
        quotes.add(quoteTwo);
        quoteValidator(quotes, uniqueQuoteMap, uniqueQuoteList);

        assertTrue(expectedList.equals(uniqueQuoteList));
    }

    @Test
    public void testBadQuoteMedia() {
        ArrayList<Quote> quotes = new ArrayList<>();
        HashMap<Integer, Quote> uniqueQuoteMap = new HashMap<>();
        ArrayList<Quote> uniqueQuoteList = new ArrayList<>();

        Quote quoteOne = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Healthruwords.com_-_Inspirational_Images_-_From-Thoughts-to-Things-300x300.jpg");
        Quote quoteTwo = new Quote(70, "http://healthruwords.com/wp-content/uploads/2016/07/Health");

        ArrayList<Quote> expectedList = new ArrayList<>();
        expectedList.add(quoteOne);

        quotes.add(quoteOne);
        quotes.add(quoteTwo);
        quoteValidator(quotes, uniqueQuoteMap, uniqueQuoteList);

        assertTrue(expectedList.equals(uniqueQuoteList));
    }

    @Test
    public void testMultiple() {
        ArrayList<Quote> quotes = new ArrayList<>();
        HashMap<Integer, Quote> uniqueQuoteMap = new HashMap<>();
        ArrayList<Quote> uniqueQuoteList = new ArrayList<>();

        Quote quoteOne = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Healthruwords.com_-_Inspirational_Images_-_From-Thoughts-to-Things-300x300.jpg");
        Quote quoteTwo = new Quote(56, "http://healthruwords.com/wp-content/uploads/2016/07/Health");
        Quote quoteThree = new Quote(8504, "http://healthruwords.com/wp-content/uploads/2015/08/healthruwords-com_-_inspirational_images_-_-hearts-beliefs-300x300.jpg");
        Quote quoteFour = new Quote(8504, "http://healthruwords.com/wp-content/uploads/2015/08/healthruwords-com_-_inspirational_images_-_-hearts-beliefs-300x300.jpg");

        quotes.add(quoteOne);
        quotes.add(quoteTwo);
        quotes.add(quoteThree);
        quotes.add(quoteFour);

        ArrayList<Quote> expectedList = new ArrayList<>();
        expectedList.add(quoteOne);
        expectedList.add(quoteThree);

        quoteValidator(quotes, uniqueQuoteMap, uniqueQuoteList);

        assertTrue(expectedList.equals(uniqueQuoteList));
    }

    @Test
    public void testAllBadIdInput() {
        ArrayList<Quote> quotes = new ArrayList<>();
        HashMap<Integer, Quote> uniqueQuoteMap = new HashMap<>();
        ArrayList<Quote> uniqueQuoteList = new ArrayList<>();

        Quote quoteOne = new Quote(8504, "http://healthruwords.com/wp-content/uploads/2015/08/healthruwords-com_-_inspirational_images_-_-hearts-beliefs-300x300.jpg");
        Quote quoteTwo = new Quote(8504, "http://healthruwords.com/wp-content/uploads/2015/08/healthruwords-com_-_inspirational_images_-_-hearts-beliefs-300x300.jpg");
        Quote quoteThree = new Quote(8504, "http://healthruwords.com/wp-content/uploads/2015/08/healthruwords-com_-_inspirational_images_-_-hearts-beliefs-300x300.jpg");
        Quote quoteFour = new Quote(8504, "http://healthruwords.com/wp-content/uploads/2015/08/healthruwords-com_-_inspirational_images_-_-hearts-beliefs-300x300.jpg");

        quotes.add(quoteOne);
        quotes.add(quoteTwo);
        quotes.add(quoteThree);
        quotes.add(quoteFour);

        // only the first quote is valid
        ArrayList<Quote> expectedList = new ArrayList<>();
        expectedList.add(quoteOne);

        quoteValidator(quotes, uniqueQuoteMap, uniqueQuoteList);

        assertTrue(expectedList.equals(uniqueQuoteList));
    }

    @Test
    public void testAllBadQuoteMedia() {
        ArrayList<Quote> quotes = new ArrayList<>();
        HashMap<Integer, Quote> uniqueQuoteMap = new HashMap<>();
        ArrayList<Quote> uniqueQuoteList = new ArrayList<>();

        Quote quoteOne = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Healthruwords");
        Quote quoteTwo = new Quote(56, "http://healthruwords.com/wp-content/uploads/2016/07/Health");
        Quote quoteThree = new Quote(8504, "http://healthruwords.com/wp-content/uploads/2015/08/healthruwords-com_-_inspirational_images");
        Quote quoteFour = new Quote(84, "http://healthruwords.com/wp-content/uploads/");

        quotes.add(quoteOne);
        quotes.add(quoteTwo);
        quotes.add(quoteThree);
        quotes.add(quoteFour);

        // nothing should be filled in the array list
        ArrayList<Quote> expectedList = new ArrayList<>();

        quoteValidator(quotes, uniqueQuoteMap, uniqueQuoteList);

        assertTrue(expectedList.equals(uniqueQuoteList));
    }

    @Test
    public void testDuplicateBadIdAndQuoteMedia() {
        ArrayList<Quote> quotes = new ArrayList<>();
        HashMap<Integer, Quote> uniqueQuoteMap = new HashMap<>();
        ArrayList<Quote> uniqueQuoteList = new ArrayList<>();

        Quote quoteOne = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Health");
        Quote quoteTwo = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Health");
        Quote quoteThree = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Health");
        Quote quoteFour = new Quote(11760, "http://healthruwords.com/wp-content/uploads/2016/07/Health");

        quotes.add(quoteOne);
        quotes.add(quoteTwo);
        quotes.add(quoteThree);
        quotes.add(quoteFour);

        ArrayList<Quote> expectedList = new ArrayList<>();

        quoteValidator(quotes, uniqueQuoteMap, uniqueQuoteList);

        assertTrue(expectedList.equals(uniqueQuoteList));
    }
}