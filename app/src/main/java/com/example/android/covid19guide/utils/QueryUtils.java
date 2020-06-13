package com.example.android.covid19guide.utils;

import android.text.TextUtils;
import android.util.Log;
import com.example.android.covid19guide.datamodels.Article;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving News' Articles Data from the Guardian's Data Set.
 */
public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because we don't want to create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * This method queries the Guardian's Data Set and return a list of {@link Article} objects.
     *
     * @param requestUrl is the url that we use to make a request to the server for the data resources needed
     * @return List<Article> is the list of News' Articles that will be displayed in the RecyclerView and which will be the data set of the ArticlesAdapter
     */
    public static List<Article> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Article}
        List<Article> articlesList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Article}
        return articlesList;
    }

    /**
     * This method returns new URL object from the given string URL.
     *
     * @param stringUrl is the String Url that needs to be converted to a URL Object
     * @return URL is the URL Object Created from the String Url
     */
    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {

            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;

        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000 /* milliseconds */);

            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);

            } else {

                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem retrieving the News Articles JSON results.", e);

        } finally {

            if (urlConnection != null) {

                urlConnection.disconnect();
            }

            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method declaration specifies that an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {

                output.append(line);

                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from parsing the given JSON response.
     */
    private static List<Article> extractFeatureFromJson(String articlesJSON) {

        // If the JSON string is empty, then return early.
        if (TextUtils.isEmpty(articlesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding News Articles to
        List<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject baseJsonResponse from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(articlesJSON);

            // Extract the JSONArray associated with the key called "results", which represents a list of results or news articles.
            JSONArray resultsArray = baseJsonResponse.getJSONObject("response").getJSONArray("results");

            // For each news article in the resultsArray, create an {@link Article} object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single Article at position i within the list of results/articles
                JSONObject currentArticle = resultsArray.getJSONObject(i);

                //Extract the value for the key called "sectionName"
                String sectionName = currentArticle.getString("sectionName");

                //Extract the value for the key called "webPublicationDate"
                String articleDate = currentArticle.getString("webPublicationDate");

                //Extract the date section only from the articleDate without the time section
                String[] datePartsArray = articleDate.split("T");

                String dateSection = datePartsArray[0];

                //Extract the value for the key called "webTitle"
                String articleTitle = currentArticle.getString("webTitle");

                //Extract the value for the key called "webUrl"
                String articleUrl = currentArticle.getString("webUrl");

                // Extract the JSONObject associated with the key called "fields"
                JSONObject fields = currentArticle.getJSONObject("fields");

                //Extract the value for the key called "bodyText"
                String articleDescription = fields.getString("bodyText");

                //Extract the value for the key called "thumbnail"
                String articleImage = fields.getString("thumbnail");

                // Extract the JSONArray associated with the key called "tags"
                JSONArray tagsArray = currentArticle.getJSONArray("tags");

                //Declare String variable to hold the article's author name
                String authorName = null;

                //Extract the value for the key called "webTitle" representing the author name
                if (tagsArray.length() != 0) {

                    JSONObject tagsElement = tagsArray.getJSONObject(0);

                    authorName = tagsElement.getString("webTitle");
                }

                // Create a new {@link Article} object with the title, description, section name, date of publishing, article's website Url,
                //article's author name, and article's image thumbnail from the JSON response.
                Article article = new Article(articleTitle, articleDescription, sectionName, dateSection, articleUrl, authorName, articleImage);

                // Add the new {@link Article} to the list of articles.
                articles.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of articles
        return articles;
    }
}
