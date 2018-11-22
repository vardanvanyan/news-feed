package vardanvanyan.com.newsfeed.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.SoftReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import vardanvanyan.com.newsfeed.R;
import vardanvanyan.com.newsfeed.adapters.NewsAdapter;
import vardanvanyan.com.newsfeed.api.models.Content;
import vardanvanyan.com.newsfeed.database.entities.News;
import vardanvanyan.com.newsfeed.providers.NewsDataProvider;
import vardanvanyan.com.newsfeed.utils.Utils;

public class NewsDetailActivity extends AppCompatActivity implements
        NewsDataProvider.DetailNewsCallback, NewsDataProvider.DetailNewsFromDBCallback {

    private static final String NO_WEB_CONTENT_HTML = "file:///android_asset/noContent.html";

    public static final String PINNED_NEWS_SHARED_PREF_KEY_FILE =
            "smbat.com.newsfeed.activities.NewsDetailActivity.PINNED_NEWS_SHARED_PREF_KEY_FILE";
    public static final String PINNED_ITEM_URL_KEY =
            "smbat.com.newsfeed.activities.NewsDetailActivity.PINNED_ITEM_URL_KEY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.news_image)
    ImageView newsImage;
    @BindView(R.id.news_title)
    TextView newsTitle;
    @BindView(R.id.news_description)
    TextView newsDescription;
    @BindView(R.id.news_web_content)
    WebView newsWebContent;
    @BindView(R.id.save_news)
    FloatingActionButton saveNewsButton;
    @BindView(R.id.pin_news)
    FloatingActionButton pinNewsButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initializeToolbar();
        initializeDataProvider();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewsDetailLoaded(Content singleNews) {
        initializeUI(singleNews);
    }

    @Override
    public void onNewsDetailLoadedFromDB(News singleNews) {
        initializeUIFromDB(singleNews);
    }

    /* Helper Methods */

    /**
     * Initializes data provider for getting available news.
     */
    private void initializeDataProvider() {
        final String apiUrl = getIntent().getStringExtra(NewsAdapter.CURRENT_ITEM_API_URL);
        final NewsDataProvider dataProvider = NewsDataProvider.getInstance();
        if (null == apiUrl) {
            final int newsId = getIntent().getIntExtra(NewsAdapter.CURRENT_ITEM_ID, 0);
            dataProvider.loadNewsDetailFromDB(this,
                    new SoftReference<Context>(this), newsId);
            return;
        }
        dataProvider.loadNewsDetail(this, apiUrl);
        sharedPreferences = getSharedPreferences(PINNED_NEWS_SHARED_PREF_KEY_FILE, MODE_PRIVATE);
    }

    /**
     * Initializes action bar and toolbar.
     */
    private void initializeToolbar() {
        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initializes UI elements using data from API request.
     *
     * @param singleNews For bind views data from single news
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initializeUI(final Content singleNews) {
        Picasso.get()
                .load(singleNews.getFields().getThumbnail())
                .placeholder(R.drawable.news_image_placeholder)
                .into(newsImage);
        newsTitle.setText(singleNews.getWebTitle());
        newsDescription.setText(singleNews.getBlocks().getBody().get(0).getBodyTextSummary());
        newsWebContent.getSettings().setJavaScriptEnabled(true);
        newsWebContent.loadUrl(singleNews.getWebUrl());
        handleButtonsClick(singleNews);
    }

    /**
     * Initializes UI elements using data from database.
     *
     * @param singleNews For bind views data from single news
     */
    private void initializeUIFromDB(final News singleNews) {
        newsImage.setImageBitmap(Utils.getBitmapFromBytes(singleNews.getNewsImage()));
        newsTitle.setText(singleNews.getNewsTitle());
        newsDescription.setText(singleNews.getNewsDescription());
        newsWebContent.loadUrl(NO_WEB_CONTENT_HTML);
    }


    /**
     * Handles "save in database" and "pin in home screen" buttons click and corresponding logic.
     *
     * @param singleNews For accessing specified news data
     */
    private void handleButtonsClick(final Content singleNews) {
        saveNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Utils.saveNewsInDB(singleNews, NewsDetailActivity.this);
                    Toast.makeText(NewsDetailActivity.this,
                            R.string.news_saved_message, Toast.LENGTH_SHORT).show();
                    saveNewsButton.setEnabled(false);
                } catch (IOException e) {
                    Log.d("Failed To Save News", e.getLocalizedMessage());
                    Toast.makeText(NewsDetailActivity.this,
                            R.string.failed_to_save, Toast.LENGTH_SHORT).show();
                }
            }
        });
        pinNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString(PINNED_ITEM_URL_KEY + singleNews.getId(), singleNews.getApiUrl()).apply();
                pinNewsButton.setEnabled(false);
                Toast.makeText(NewsDetailActivity.this,
                        R.string.news_pinned_message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
