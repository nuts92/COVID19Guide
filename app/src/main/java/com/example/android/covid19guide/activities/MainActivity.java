package com.example.android.covid19guide.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.example.android.covid19guide.R;
import com.example.android.covid19guide.adapters.ArticlesAdapter;
import com.example.android.covid19guide.datamodels.Article;
import com.example.android.covid19guide.loaders.ArticlesLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity class displays the Latest News' Articles regarding the Coronavirus from the Guardian API where the
 * user can click on any of the articles and read more on the Guardian's Website.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    /**
     * URL for the News' Articles Data from the Guardian Data Set
     */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    /**
     * Constant value for the Articles loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int ARTICLES_LOADER_ID = 1;

    /**
     * Adapter for the list of News' Articles
     */
    private ArticlesAdapter mAdapter;

    /**
     * The Layout that is displayed when the list is empty or there is no internet connection
     */
    private ConstraintLayout mEmptyStateLayout;

    /**
     * The animation that is displayed when the list is empty or there is no internet connection
     */
    private LottieAnimationView mAnimationView;

    /**
     * The TextView that is displayed when the list is empty or there is no internet connection
     */
    private TextView mEmptyStateTextView;

    /**
     * Declaring an instance of the ProgressBar that is displayed to the user until the data finishes loading
     */
    private ProgressBar mProgressBar;

    /**
     * Declaring an instance of the RecyclerView that will display the list of the News' Articles
     */
    private RecyclerView mArticlesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing All Object Variables
        mArticlesRecyclerView = findViewById(R.id.recycler_view);

        mEmptyStateLayout = findViewById(R.id.empty_state_layout);

        mAnimationView = findViewById(R.id.empty_state_animation);

        mEmptyStateTextView = findViewById(R.id.empty_state_text_view);

        mProgressBar = findViewById(R.id.loading_indicator);

        //Set the layoutManager that the recyclerView will use
        mArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //This setting improves performance if the changes in content do not change the layout size of the RecyclerView
        mArticlesRecyclerView.setHasFixedSize(true);

        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new ArticlesAdapter(this, new ArrayList<Article>());

        //Attaching the adapter to the recyclerView with the setAdapter() method.
        mArticlesRecyclerView.setAdapter(mAdapter);

        // If there is a network connection, fetch data
        if (isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLES_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide the progress bar so that the error message will be visible
            mProgressBar.setVisibility(View.GONE);

            // Update the mEmptyStateTextView with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

            // Update the mAnimationView with the no connection animation
            mAnimationView.setAnimation(R.raw.no_internet);

            // Set the visibility of the mArticlesRecyclerView to GONE
            mArticlesRecyclerView.setVisibility(View.GONE);

            // Set the visibility of the mEmptyStateLayout to VISIBLE
            mEmptyStateLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method instantiates and return a new Loader for the given ID
     *
     * @param id   int: The ID whose loader is to be created.
     * @param args Bundle: Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        //Create a Uri object from the GUARDIAN_REQUEST_URL and then get a Uri.Builder from this Uri Object
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        //Append or add all the query parameters to the uriBuilder
        uriBuilder.appendQueryParameter("q", "covid");

        uriBuilder.appendQueryParameter("format", "json");

        uriBuilder.appendQueryParameter("order-by", "relevance");

        uriBuilder.appendQueryParameter("from-date", "2020-06-01");

        uriBuilder.appendQueryParameter("show-tags", "contributor");

        uriBuilder.appendQueryParameter("show-fields", "body-text,thumbnail");

        uriBuilder.appendQueryParameter("api-key", "c7393c36-e790-4c7b-8c59-a652141fa8ad");

        // Create and return a new ArticlesLoader for the given URL
        return new ArticlesLoader(this, uriBuilder.toString());
    }

    /**
     * This method is called when a previously created loader has finished its load
     *
     * @param loader Loader<List<Article>>: The Loader that has finished.
     * @param data   List<Article>: The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {

        // Hide the progress bar because the data has been loaded
        mProgressBar.setVisibility(View.GONE);

        // Set the visibility of the mEmptyStateLayout to GONE
        mEmptyStateLayout.setVisibility(View.GONE);

        //If there is a network connection, update the mAdapter
        if (isConnected()) {

            // Clear the adapter of previous News' Articles' Data
            mAdapter.clear();

            // If there is a valid list of {@link Article}s, then add them to the adapter's
            // data set so that they will be displayed.
            if (data != null && !data.isEmpty()) {

                mAdapter.addAll(data);

                mArticlesRecyclerView.setVisibility(View.VISIBLE);

            } else {

                // Update the mEmptyStateTextView with no articles are found message
                mEmptyStateTextView.setText(R.string.no_news_articles);

                // Update the mAnimationView with the empty state animation
                mAnimationView.setAnimation(R.raw.empty_state);

                // Set the visibility of the mArticlesRecyclerView to GONE
                mArticlesRecyclerView.setVisibility(View.GONE);

                // Set the visibility of the mEmptyStateLayout to VISIBLE
                mEmptyStateLayout.setVisibility(View.VISIBLE);
            }

        } else {

            // Update the mEmptyStateTextView with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

            // Update the mAnimationView with the no connection animation
            mAnimationView.setAnimation(R.raw.no_internet);

            // Set the visibility of the mArticlesRecyclerView to GONE
            mArticlesRecyclerView.setVisibility(View.GONE);

            // Set the visibility of the mEmptyStateLayout to VISIBLE
            mEmptyStateLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method is called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader Loader<List<Article>>: The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    /**
     * This  method checks for the connectivity status if there is a network connection or not
     *
     * @return boolean: true if there is a network connection so that we can fetch data
     */
    private boolean isConnected() {

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = null;

        if (connectivityManager != null) {

            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        // return the status of the network connection whether there is a network connection or not
        return networkInfo != null && networkInfo.isConnected();
    }
}
