package vardanvanyan.com.newsfeed.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.List;

import vardanvanyan.com.newsfeed.database.AppDataBase;
import vardanvanyan.com.newsfeed.database.entities.News;
import vardanvanyan.com.newsfeed.providers.NewsDataProvider;

/**
 * Task to load news from database for application offline support.
 */
public class LoadNewsFromDBTask extends AsyncTask<Void, Void, List<News>> {

    private final NewsDataProvider.NewsFromDBCallback callback;
    private final SoftReference<Context> context;

    public LoadNewsFromDBTask(final NewsDataProvider.NewsFromDBCallback callback,
                              final SoftReference<Context> context) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected List<News> doInBackground(Void... voids) {
        final AppDataBase appDataBase = AppDataBase.getAppDatabase(context.get());
        final News[] allSavedNews = appDataBase.newsDao().loadAll();
        return Arrays.asList(allSavedNews);
    }

    @Override
    protected void onPostExecute(List<News> news) {
        super.onPostExecute(news);
        callback.onNewsLoadedFromDB(news);
    }
}
