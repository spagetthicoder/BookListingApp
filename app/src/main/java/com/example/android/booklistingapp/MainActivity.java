package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    /**
     * Constant value for the Book loader ID.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * URL for Book data from the Google Books API
     */
    private static String googleBooks_request_url =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=40";

    private String searchBook;

    private boolean isConected;

    private boolean isConectedSearchButton;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        isConected = networkInfo != null && networkInfo.isConnected();

        final Button searchButton = (Button) findViewById(R.id.search_button);

        final ListView listView = (ListView) findViewById(R.id.list);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr1 = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo1 = connMgr1.getActiveNetworkInfo();

                isConectedSearchButton = networkInfo1 != null && networkInfo1.isConnected();

                if (isConectedSearchButton) {
                    EditText searchField = (EditText) findViewById(R.id.search_field);
                    searchBook = searchField.getText().toString();

                    googleBooks_request_url = "https://www.googleapis.com/books/v1/volumes?q=" + searchBook + "&maxResults=40";

                    View loadingIndicator = findViewById(R.id.loading_spinner);
                    loadingIndicator.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);

                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                    Toast.makeText(MainActivity.this, "Please wait until the data gets fetched!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No Internet connection available! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes the list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);

        // If there is a network connection, fetch data
        if (isConected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "Test: onCreateLoader() called ...");

        // Create a new loader for the given URL
        return new BookLoader(this, googleBooks_request_url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        ProgressBar mCirleProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mCirleProgressBar.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
