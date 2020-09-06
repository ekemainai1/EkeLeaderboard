package com.example.ekeleaderboard.networkcalls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static String SUB_URL = "https://docs.google.com/forms/d/e/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setLenient()
            .create();

    // Retrofit initialization for posting project
    static Retrofit provideRetrofitPost() {
        String BASE_URI = SUB_URL;
        HttpLoggingInterceptor interceptorLog = new HttpLoggingInterceptor();
        Interceptor interceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request newRequest = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", String.valueOf(JSON))
                        .build();
                return chain.proceed(newRequest);
            }
        };

        interceptorLog.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(interceptorLog)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

    }

    public static APIInterface getClient() {

        Retrofit adapter = provideRetrofitPost();

        //Creating object for our interface
        APIInterface api = adapter.create(APIInterface.class);
        return api; // return the APIInterface object
    }
}
