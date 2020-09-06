package com.example.ekeleaderboard.networkcalls;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ekeleaderboard.entity.LearningLeadersEntity;
import com.example.ekeleaderboard.entity.SkillIQLeadersEntity;
import com.example.ekeleaderboard.models.LearnersObject;
import com.example.ekeleaderboard.models.SkillIqObject;
import com.example.ekeleaderboard.repo.LoadLocalRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static String SYS_IP = "https://gadsapi.herokuapp.com";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private List<LearnersObject> learnersObject;
    private List<SkillIqObject> skillIqObject;
    LearningLeadersEntity leadersEntity;
    SkillIQLeadersEntity iqLeadersEntity;
    private Call<List<LearnersObject>> callLearn;
    private Call<List<SkillIqObject>> callSkillIq;
    private APIInterface apiInterface;


    Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setLenient()
            .create();

    Retrofit provideRetrofit() {
        String BASE_URI = SYS_IP;
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

    // Constructor
    public Repository() {
        Retrofit retrofit = provideRetrofit();
        apiInterface = retrofit.create(APIInterface.class);
        callLearn = apiInterface.getLearningLeadersData();
        callSkillIq = apiInterface.getSkillIOLeadersData();

    }



    // Call to learners
    public List<LearnersObject> invokeLearnersList(LoadLocalRepository localRepository) {

        callLearn.clone().enqueue(new Callback<List<LearnersObject>>() {
            @Override
            public void onResponse(@NonNull Call<List<LearnersObject>> callLearn,
                                   @NonNull retrofit2.Response<List<LearnersObject>> response) {
                if (response.isSuccessful()) {

                    learnersObject = new ArrayList<>();
                    assert response.body() != null;
                    learnersObject.addAll(response.body());
                    for (int x = 0; x < learnersObject.size(); x++) {
                        // Create data object for insertion into database
                        leadersEntity = new LearningLeadersEntity(
                                learnersObject.get(x).getName(),
                                learnersObject.get(x).getHours(),
                                learnersObject.get(x).getCountry(),
                                learnersObject.get(x).getBadgeUrl()
                        );

                        Log.e("LEARNERS", learnersObject.get(x).getName());

                        // Insert learners object into database
                        Single.fromCallable(() -> localRepository
                                .insertLocalLearners(leadersEntity))
                                .subscribeOn(Schedulers.io())
                                .subscribe();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<LearnersObject>> callLearn, @NotNull Throwable throwable) {
                Log.e("learners Error", throwable.toString());
            }
        });

        return learnersObject;
    }

    // Call to skillIq
    public List<SkillIqObject> invokeSkillIqList(LoadLocalRepository localRepository) {

        callSkillIq.clone().enqueue(new Callback<List<SkillIqObject>>() {
            @Override
            public void onResponse(@NonNull Call<List<SkillIqObject>> callSkillIq,
                                   @NonNull retrofit2.Response<List<SkillIqObject>> response) {
                if (response.isSuccessful()) {

                    skillIqObject = new ArrayList<>();
                    assert response.body() != null;
                    skillIqObject.addAll(response.body());
                    for (int x = 0; x < skillIqObject.size(); x++) {
                        // Create data object for insertion into database
                        iqLeadersEntity = new SkillIQLeadersEntity(
                                skillIqObject.get(x).getName(),
                                skillIqObject.get(x).getScore(),
                                skillIqObject.get(x).getCountry(),
                                skillIqObject.get(x).getBadgeUrl()
                        );

                        Log.e("SKILLIQ", skillIqObject.get(x).getName());

                        // Insert skillIq object into database
                        Single.fromCallable(() -> localRepository
                                .insertLocalSkillIq(iqLeadersEntity))
                                .subscribeOn(Schedulers.io())
                                .subscribe();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<SkillIqObject>> callSkillIq, @NotNull Throwable throwable) {
                Log.e("skillIq Error", throwable.toString());
            }
        });

        return skillIqObject;
    }


}
