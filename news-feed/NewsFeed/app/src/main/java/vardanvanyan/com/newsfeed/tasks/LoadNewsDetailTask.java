package vardanvanyan.com.newsfeed.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vardanvanyan.com.newsfeed.api.NewsClient;
import vardanvanyan.com.newsfeed.api.ServiceGenerator;
import vardanvanyan.com.newsfeed.api.models.Content;
import vardanvanyan.com.newsfeed.api.models.Result;
import vardanvanyan.com.newsfeed.api.models.SingleNews;
import vardanvanyan.com.newsfeed.constants.Constants;
import vardanvanyan.com.newsfeed.providers.NewsDataProvider;

/**
 * Task to load news detailed information (content) by calling API request for specified news.
 */
public class LoadNewsDetailTask extends AsyncTask<Void, Void, Result> {

    private final NewsDataProvider.DetailNewsCallback callback;
    private final String singleNewsAPIUrl;
    private Content newsContent;

    public LoadNewsDetailTask(final NewsDataProvider.DetailNewsCallback callback,
                              final String singleNewsAPIUrl) {

        this.callback = callback;
        this.singleNewsAPIUrl = singleNewsAPIUrl;
    }

    @Override
    protected Result doInBackground(Void... voids) {
        final NewsClient client =
                ServiceGenerator.createService(NewsClient.class);
        final Call<SingleNews> call = client.getSingleNewsJson(singleNewsAPIUrl, Constants.API_KEY,
                Constants.SHOW_BLOCKS, Constants.SHOW_FIELDS_THUMBNAIL);
        call.enqueue(new Callback<SingleNews>() {
            @Override
            public void onResponse(@NonNull Call<SingleNews> call, @NonNull Response<SingleNews> response) {
                final SingleNews body = response.body();
                if (null == body) {
                    return;
                }
                newsContent = body.getResponse().getContent();
                callback.onNewsDetailLoaded(newsContent);
            }

            @Override
            public void onFailure(@NonNull Call<SingleNews> call, @NonNull Throwable t) {
                Log.e("Fail to get results: ", t.getMessage());
            }
        });
        return null;
    }
}
