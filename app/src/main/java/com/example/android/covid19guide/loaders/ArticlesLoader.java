package com.example.android.covid19guide.loaders;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;
import com.example.android.covid19guide.datamodels.Article;
import com.example.android.covid19guide.utils.QueryUtils;
import java.util.List;

/**
 * Loads a list of News' Articles by using an AsyncTask to perform the network request to the given URL.
 */
public class ArticlesLoader extends AsyncTaskLoader<List<Article>> {

    /**
     * mUrl represents the Query URL that we use to make a request to the server for the data resources needed
     */
    private String mUrl;

    /**
     * Constructs a new {@link ArticlesLoader}.
     *
     * @param context of the activity
     * @param url     to load data from the Guardian API
     */
    public ArticlesLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     * This method will normally be called for you automatically by LoaderManager when the associated fragment/activity is being started.
     */
    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    /**
     * This is on a background thread. This method is called on a worker thread to perform the actual load and
     * to return the result of the load operation.
     */
    @Nullable
    @Override
    public List<Article> loadInBackground() {

        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of news' articles.
        return QueryUtils.fetchNewsData(mUrl);
    }
}





