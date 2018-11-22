package vardanvanyan.com.newsfeed.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Service generator so that only one web socket is opened throughout the application
 */
public class ServiceGenerator {

    private ServiceGenerator() {
        throw new UnsupportedOperationException();
    }

    private static final String BASE_URL = "https://content.guardianapis.com/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(final Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
