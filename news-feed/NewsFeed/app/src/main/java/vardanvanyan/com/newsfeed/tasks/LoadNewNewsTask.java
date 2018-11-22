package vardanvanyan.com.newsfeed.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vardanvanyan.com.newsfeed.api.NewsClient;
import vardanvanyan.com.newsfeed.api.ServiceGenerator;
import vardanvanyan.com.newsfeed.api.models.News;
import vardanvanyan.com.newsfeed.constants.Constants;
import vardanvanyan.com.newsfeed.providers.NewsDataProvider;

/**
 * Task to check is new news are available by calling API request and comparing page total size.
 */
public class LoadNewNewsTask extends AsyncTask<Void, Void, Integer> {

    private static final int FIRST_PAGE = 1;

    private final NewsDataProvider.NewsNotificationCallback callback;
    private Integer newsCount;

    public LoadNewNewsTask(final NewsDataProvider.NewsNotificationCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        final NewsClient client = ServiceGenerator.createService(NewsClient.class);
        final Call<News> call =
                client.getBaseJson(Constants.API_KEY, Constants.SHOW_FIELDS_THUMBNAIL, FIRST_PAGE);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                final News body = response.body();
                if (null == body) {
                    return;
                }
                newsCount = body.getResponse().getPageSize();
                callback.onNewNewsLoaded(newsCount);
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                Log.e("Fail to get response: ", t.getMessage());
            }
        });
        return newsCount;
    }
}
