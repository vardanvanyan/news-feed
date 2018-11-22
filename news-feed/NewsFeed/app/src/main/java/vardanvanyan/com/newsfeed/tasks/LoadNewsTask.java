package vardanvanyan.com.newsfeed.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vardanvanyan.com.newsfeed.api.NewsClient;
import vardanvanyan.com.newsfeed.api.ServiceGenerator;
import vardanvanyan.com.newsfeed.api.models.News;
import vardanvanyan.com.newsfeed.api.models.Result;
import vardanvanyan.com.newsfeed.constants.Constants;
import vardanvanyan.com.newsfeed.providers.NewsDataProvider;

/**
 * Task to load news by calling API requests according to pagination.
 */
public class LoadNewsTask extends AsyncTask<Void, Void, List<Result>>  {

    private static int pageSize = 0;

    private final NewsDataProvider.NewsCallback callback;
    private List<Result> results;

    public LoadNewsTask(final NewsDataProvider.NewsCallback callback) {
        this.callback = callback;
        pageSize++;
    }

    @Override
    protected List<Result> doInBackground(Void... voids) {
        final NewsClient client = ServiceGenerator.createService(NewsClient.class);
        final Call<News> call =
                client.getBaseJson(Constants.API_KEY, Constants.SHOW_FIELDS_THUMBNAIL, pageSize);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                final News body = response.body();
                if (null == body) {
                    return;
                }
                results = body.getResponse().getResults();
                callback.onNewsLoaded(results);
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                Log.e("Fail to get results: ", t.getMessage());
            }
        });
        return results;
    }
}
