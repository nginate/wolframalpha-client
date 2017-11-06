package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.retrofit.PayloadAdapter;
import com.github.nginate.wolframalpha.retrofit.converter.factory.ByteArrayConverterFactory;
import com.github.nginate.wolframalpha.retrofit.converter.factory.FullApiConverterFactory;
import com.github.nginate.wolframalpha.retrofit.interceptor.DocumentedErrorsInterceptor;
import com.github.nginate.wolframalpha.retrofit.interceptor.LoggingInterceptor;
import com.github.nginate.wolframalpha.shortanswer.ShortAnswersApi;
import com.github.nginate.wolframalpha.simple.SimpleApi;
import com.github.nginate.wolframalpha.spoken.SpokenResultsApi;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Utility configurations to provide client for particular APIs
 */
@UtilityClass
public class ClientFactory {

    private static final String url = "https://api.wolframalpha.com";

    /**
     * Build Simple API client using default API url (https://api.wolframalpha.com) and log level (FULL)
     *
     * @return Simple API client
     */
    public static SimpleApi simpleApiClient() {
        return simpleApiClient(url);
    }

    /**
     * Build Simple API client
     *
     * @param url API url
     * @return Simple API client
     */
    public static SimpleApi simpleApiClient(String url) {
        Retrofit retrofit = getRetrofit(url)
                .addConverterFactory(ByteArrayConverterFactory.create())
                .build();
        return retrofit.create(SimpleApi.class);
    }

    /**
     * Build Spoken results API client using default API url (https://api.wolframalpha.com
     *
     * @return Spoken results API client
     */
    public static SpokenResultsApi spokenResultsApi() {
        return spokenResultsApi(url);
    }

    /**
     * Build Spoken results API client
     *
     * @param url API url
     * @return Spoken results API client
     */
    public static SpokenResultsApi spokenResultsApi(String url) {
        Retrofit retrofit = getRetrofit(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit.create(SpokenResultsApi.class);
    }

    /**
     * Build Short answers API client
     *
     * @return Short answers API client
     */
    public static ShortAnswersApi shortAnswersApi() {
        return shortAnswersApi(url);
    }

    /**
     * Build Short answers API client
     *
     * @param url API url
     * @return Short answers API client
     */
    public static ShortAnswersApi shortAnswersApi(String url) {
        Retrofit retrofit = getRetrofit(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit.create(ShortAnswersApi.class);
    }

    /**
     * Build Full results API client
     *
     * @return Full results API client
     */
    public static FullResultsApi fullResultsApi() {
        return fullResultsApi(url);
    }

    /**
     * Build Full results API client
     *
     * @param url API url
     * @return Full results API client
     */
    public static FullResultsApi fullResultsApi(String url) {
        Retrofit retrofit = getRetrofit(url)
                .addConverterFactory(FullApiConverterFactory.create())
                .addCallAdapterFactory(new PayloadAdapter())
                .build();
        return retrofit.create(FullResultsApi.class);
    }

    private static Retrofit.Builder getRetrofit(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new DocumentedErrorsInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client);
    }
}
