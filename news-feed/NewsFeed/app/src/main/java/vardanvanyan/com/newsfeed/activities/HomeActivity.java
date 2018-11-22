package vardanvanyan.com.newsfeed.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vardanvanyan.com.newsfeed.R;
import vardanvanyan.com.newsfeed.adapters.NewsAdapter;
import vardanvanyan.com.newsfeed.api.models.Result;
import vardanvanyan.com.newsfeed.database.entities.News;
import vardanvanyan.com.newsfeed.providers.NewsDataProvider;
import vardanvanyan.com.newsfeed.services.NewsService;
import vardanvanyan.com.newsfeed.utils.Utils;

import static vardanvanyan.com.newsfeed.services.NewsService.NEWS_LOADED_INTENT_ACTION;

public class HomeActivity extends AppCompatActivity implements NewsDataProvider.NewsCallback,
        NewsDataProvider.PinnedNewsCallback, NewsDataProvider.NewsFromDBCallback {

    private static final int PAGE_SIZE = 6;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.pin_items_list)
    RecyclerView pinnedNewsRecyclerView;
    @BindView(R.id.news_items_list)
    RecyclerView newsRecyclerView;

    private boolean isInGridMode = false;
    private NewsAdapter newsListAdapter;
    private NewsAdapter pinnedNewsAdapter;
    private List<Result> pinnedList = new ArrayList<>();
    private List<Result> newsList = new ArrayList<>();
    private NewsDataProvider dataProvider;
    private RecyclerView.LayoutManager layoutManager;
    private MenuItem listModeMenuItem;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utils.isNetworkAvailable(HomeActivity.this)) {
                dataProvider.loadNews(HomeActivity.this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initializeDataProvider();
        initializeNewsListView();
        initializePinnedNewsListView();
        startNewsService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        listModeMenuItem = menu.findItem(R.id.action_view_list);
        initializeSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_list) {
            if (isInGridMode) {
                makeListMode();
                return true;
            }
            makeGridMode();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewsLoaded(final List<Result> newNewsList) {
        newsList.addAll(newNewsList);
        if (null != newsListAdapter) {
            newsListAdapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onNewsLoadedFromDB(List<News> newsList) {
        initializeNewsListViewFromDB(newsList);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(
                broadcastReceiver, new IntentFilter(NEWS_LOADED_INTENT_ACTION));
        if (Utils.isNetworkAvailable(this)) {
            pinnedList.clear();
            dataProvider.loadPinnedNews(this, this);
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onPinnedNewsLoaded(Result newPinnedNews) {
        pinnedList.add(newPinnedNews);
        if (null != pinnedNewsAdapter) {
            pinnedNewsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !isInGridMode) {
            makeGridMode();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && isInGridMode) {
            makeListMode();
        }
    }

    /* Helper Methods */

    /**
     * Initializes data provider for getting available news.
     */
    private void initializeDataProvider() {
        dataProvider = NewsDataProvider.getInstance();
        if (Utils.isNetworkAvailable(this)) {
            dataProvider.loadNews(this);
            dataProvider.loadPinnedNews(this, this);
            return;
        }
        dataProvider.loadNewsFromDB(this, new SoftReference<Context>(this));
    }

    /**
     * Starts news foreground service for getting new added news immediately.
     */
    private void startNewsService() {
        final Intent intent = new Intent(this, NewsService.class);
        startService(intent);
    }

    /**
     * Initializes news list view using data from API request.
     */
    private void initializeNewsListView() {
        layoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setHasFixedSize(true);
        newsListAdapter = new NewsAdapter(this, newsList);
        newsRecyclerView.setAdapter(newsListAdapter);
        newsRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    }

    /**
     * Initializes pinned news list view using data from API request and shared prefs.
     */
    private void initializePinnedNewsListView() {
        pinnedNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        pinnedNewsRecyclerView.setHasFixedSize(true);
        pinnedNewsAdapter = new NewsAdapter(this, pinnedList, true);
        pinnedNewsRecyclerView.setAdapter(pinnedNewsAdapter);
    }

    /**
     * Initializes news list view using data from database.
     *
     * @param newsList news list to iterate and fill adapter
     */
    private void initializeNewsListViewFromDB(final List<News> newsList) {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setHasFixedSize(true);
        final List<Result> results = new ArrayList<>();
        for (final News savedNews : newsList) {
            final Result result = new Result();
            result.setWebTitle(savedNews.getNewsTitle());
            result.setSectionName(savedNews.getNewsCategory());
            result.setDescription(savedNews.getNewsDescription());
            result.setImageBytes(savedNews.getNewsImage());
            results.add(result);
        }
        newsListAdapter = new NewsAdapter(this, results);
        newsRecyclerView.setAdapter(newsListAdapter);
    }

    /**
     * Initializes search view and listened search query changes, makes filter over news and
     * pinned news lists.
     *
     * @param menu toolbar's menu search item
     */
    private void initializeSearchView(final Menu menu) {
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newsListAdapter.getFilter().filter(query);
                pinnedNewsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                newsListAdapter.getFilter().filter(query);
                pinnedNewsAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    /**
     * Makes news list view grid mode.
     */
    private void makeGridMode() {
        layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        newsRecyclerView.setLayoutManager(layoutManager);
        listModeMenuItem.setIcon(R.drawable.ic_grid);
        isInGridMode = true;
    }

    /**
     * Makes news list view ordinal list mode.
     */
    private void makeListMode() {
        layoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(layoutManager);
        listModeMenuItem.setIcon(R.drawable.ic_list);
        isInGridMode = false;
    }

    /**
     * Listener for handling recycler view position changing and makes new API requests for
     * receiving new news data (pagination).
     */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final int visibleItemCount = layoutManager.getChildCount();
            final int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = 0;
            if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItemPosition =
                        ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else {
                final int[] positions = new int[2];
                ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions);
                firstVisibleItemPosition = positions[0];
            }
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                dataProvider.loadNews(HomeActivity.this);
            }
        }
    };

}
