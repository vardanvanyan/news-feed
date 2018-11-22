package vardanvanyan.com.newsfeed.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import vardanvanyan.com.newsfeed.api.models.News;
import vardanvanyan.com.newsfeed.api.models.SingleNews;

/**
 * Retrofit News Client for calling API request with dynamic parameters.
 */
public interface NewsClient {

    @GET("search")
    Call<News> getBaseJson(
            // API key should always be constant
            @Query("api-key") String apiKey,
            // For getting thumbnail image
            @Query("show-fields") String fields,
            // For pagination
            @Query("page") int page);

    @GET
    Call<SingleNews> getSingleNewsJson(
            @Url String url,
            // API key should always be constant
            @Query("api-key") String apiKey,
            // For getting all content
            @Query("show-blocks") String showBlock,
            // For getting thumbnail image
            @Query("show-fields") String fields);
}
