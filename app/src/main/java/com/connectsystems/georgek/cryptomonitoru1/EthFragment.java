package com.connectsystems.georgek.cryptomonitoru1;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.AnimationUtilsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.connectsystems.georgek.cryptomonitoru1.cardview.CardActivity;
import com.connectsystems.georgek.cryptomonitoru1.data.CryptoContract;
import com.connectsystems.georgek.cryptomonitoru1.data.CryptoCurrencyDBHelper;

import java.util.Objects;


/**
 * Created by george on 10/10/17.
 */

public class EthFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * Constant value for the github loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int ETH_FRAGMENT_LOADER_ID = 3;

    /**
     * String to identify intent source
     */
    private static final String ETH_CODE = "eth_value";
    LoaderManager loaderManager;

    /**
     * Adapter for the list of currencies values gotten from database
     */
    private EthCurrencyAdapter mAdapter;
    /**
     * Create an instance of CryptoCurrencyDBHelper
     */
    private CryptoCurrencyDBHelper mDBHelper;
    /**
     * TextView that is displayed when the list is empty
     */
    private View mEmptyStateTextView;
    /**
     * Progressbar that is displayed before loader loads data
     */
    private ProgressBar progressBar;
    /**
     * SwipeRefreshLayout
     */
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private EthCurrencyUpdateBroadcastReceiver mEthCurrencyUpdateBroadcastReceiver;

    private FloatingActionButton mFab;



    public EthFragment() {

        //Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.currency_base, container, false);

        // Register the intent here
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HomeActivity.MY_INTENT);
        Objects.requireNonNull(getActivity()).registerReceiver(mEthCurrencyUpdateBroadcastReceiver, intentFilter);

        mEthCurrencyUpdateBroadcastReceiver =  new EthCurrencyUpdateBroadcastReceiver();

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);


        //check internet connection
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

        HomeActivity.startedWithNetwork = activeNetwork != null && activeNetwork.isConnectedOrConnecting();



        mFab = rootView.findViewById(R.id.floatingActionButton);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // currency_base.xml layout file.
        final ListView listView = rootView.findViewById(R.id.list);

        // Get the empty  Text view
        mEmptyStateTextView = rootView.findViewById(R.id.empty);
        //Add a nice empty view to the layout file for this fragment
        listView.setEmptyView(mEmptyStateTextView);

        progressBar = rootView.findViewById(R.id.loading_spinner);

        // Create an {@link BtcCurrencyAdapter}, whose data source is a list of {@link Currency}.
        // The adapter knows how to create the list items for each item in the list.
        mAdapter = new EthCurrencyAdapter(getContext(), null, false);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(mAdapter);
        // Notify listview of data changes on adapter
        mAdapter.notifyDataSetChanged();


        // Respond to click event on user item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get the current currency that was clicked
                int number = (int) mAdapter.getItemId(position);

                // Create new intent to view CardView
                Intent cardViewIntent = new Intent(rootView.getContext(), CardActivity.class);

                // Send the "eth_value" to CardView so the right database columns will be accessed
                // and send the column name too so the Spinner default value
                // will be set.
                cardViewIntent.putExtra("CURRENCY_CODE", ETH_CODE);
                cardViewIntent.putExtra("COLUMN_NAME", number);

                startActivity(cardViewIntent);


            }
        });


        if (mAdapter.isEmpty()) {

            //****LoadManager will load information****
            // Get a reference to the loader manager in order to interact with loaders

            loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

            if (HomeActivity.startedWithNetwork) {
                loaderManager.initLoader(ETH_FRAGMENT_LOADER_ID, null, this);
                // Notify listview of data changes in adapter
                mAdapter.notifyDataSetChanged();

            } else {
                loaderManager.restartLoader(ETH_FRAGMENT_LOADER_ID, null, this);
                // Notify listview of data changes in adapter
                mAdapter.notifyDataSetChanged();

            }

        }



        /*
          Use this code to prevent SwipeRefreshLayout from interfering with
          Scrolling in ListView
         */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (listView.getChildAt(0) != null) {

                    mySwipeRefreshLayout.setEnabled(listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0);
                    // Hide floating button when top of activity
                    // is reached
                }

                if (firstVisibleItem == 0) {
                    View v = listView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        mFab.hide();
                    }
                } else if (totalItemCount - visibleItemCount > firstVisibleItem) {
                    mFab.show();
                }
            }
        });

         /*
          Used this to scroll to the top
          of the page
         */
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Scroll to activity top
                v = listView.getChildAt(0);
                int offset = (v == null) ? 0 : v.getTop();
                listView.smoothScrollToPosition(offset);


            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).registerReceiver(mEthCurrencyUpdateBroadcastReceiver, new IntentFilter(HomeActivity.MY_INTENT));
        getLoaderManager().restartLoader(ETH_FRAGMENT_LOADER_ID, null, this);
        // Notify listview of data changes in adapter
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getActivity()).registerReceiver(mEthCurrencyUpdateBroadcastReceiver, new IntentFilter(HomeActivity.MY_INTENT));
        getLoaderManager().restartLoader(ETH_FRAGMENT_LOADER_ID, null, this);
        // Notify listview of data changes in adapter
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mEthCurrencyUpdateBroadcastReceiver);
        getLoaderManager().restartLoader(ETH_FRAGMENT_LOADER_ID, null, this);

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        // Set the color of the swiperefresh animation
        mySwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorSecondaryDark);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                userPageRefreshAction();
                //onCreateLoader(1, getArguments());
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies which columns from the
        // database that will be used after this query
        String[] projection = {
                CryptoContract.CurrencyEntry._ID,
                CryptoContract.CurrencyEntry.COLUMN_CURRENCY_NAME,
                CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE
        };

        return new CursorLoader(Objects.requireNonNull(getContext()),
                CryptoContract.CurrencyEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

//        mAdapter.swapCursor(data);
        mAdapter.changeCursor(data);

        // Stop the refreshing animation
        mySwipeRefreshLayout.setRefreshing(false);

        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        // Clears out the adapter's reference to the Cursor.
        // This prevents memory leaks.
//        mAdapter.swapCursor(null);
        mAdapter.changeCursor(null);

    }

    public void userPageRefreshAction() {



        // Get a reference to the loader manager in order to interact with loaders
        //TODO: find out why this is deprecated.
        loaderManager = getLoaderManager();

        if (HomeActivity.startedWithNetwork) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.restartLoader(ETH_FRAGMENT_LOADER_ID, null, this);
            // Notify listview of data changes in adapter
            mAdapter.notifyDataSetChanged();
        } else {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ETH_FRAGMENT_LOADER_ID, null, this);
            // Notify listview of data changes in adapter
            mAdapter.notifyDataSetChanged();
        }
    }


    private void receiverLoad() {
        if (HomeActivity.startedWithNetwork) {
            getLoaderManager().restartLoader(ETH_FRAGMENT_LOADER_ID, null, this);

        }  else {
            getLoaderManager().initLoader(ETH_FRAGMENT_LOADER_ID, null, this);

        }
        getLoaderManager().getLoader(ETH_FRAGMENT_LOADER_ID);
    }

    private class EthCurrencyUpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (Objects.equals(intent.getAction(), HomeActivity.MY_INTENT)) {


                receiverLoad();
            }



        }


    }

}
