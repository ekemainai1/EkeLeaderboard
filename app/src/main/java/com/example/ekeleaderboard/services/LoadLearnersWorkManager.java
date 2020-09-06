package com.example.ekeleaderboard.services;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ekeleaderboard.repo.LoadLocalRepository;
import com.example.ekeleaderboard.networkcalls.Repository;
import com.example.ekeleaderboard.utils.SpokenNetworkUtils;
import com.example.ekeleaderboard.models.LearnersObject;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoadLearnersWorkManager extends Worker {

    Repository repository;
    LoadLocalRepository localRepository;
    private CompositeDisposable disposables;
    SpokenNetworkUtils spokenNetworkUtils;
    List<LearnersObject> learnersObjects;
    boolean network;


    public LoadLearnersWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        learnersObjects = new ArrayList<>();
        disposables = new CompositeDisposable();
        repository = new Repository();
        spokenNetworkUtils = new SpokenNetworkUtils();
        network = spokenNetworkUtils.isNetworkOnline(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Fetching Data from Remote host");

        try {
            if (network) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    Toast.makeText(getApplicationContext(),
                    "Android Version not supported", Toast.LENGTH_LONG).show();

                } else {
                    learnersBackgroundTaskScheduler(getApplicationContext());
                }


            } else {
                Toast.makeText(getApplicationContext(),
                        "Network is not available!", Toast.LENGTH_LONG).show();
            }
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Error fetching data", e);
        }
        return Result.failure();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this LIVE worker");
    }

    // Background task for learners data
    public void learnersBackgroundTaskScheduler(Context application) {

        disposables.add(learnersObservable(application)
                // Run on a background thread
                .subscribeOn(Schedulers.computation())
                // Be notified on the main thread
                .observeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<LearnersObject>>() {
                                   @Override public void onComplete() {
                                       Log.d(TAG, "onComplete()");
                                   }

                                   @Override public void onError(Throwable e) {
                                       Log.e(TAG, "onError()", e);
                                   }

                                   @Override public void onNext(List<LearnersObject> learnersObjects) {
                                       Log.d(TAG, "onNext(" + learnersObjects+ ")");

                                   }
                               }
                )
        );
    }

    // Learners observables
    Observable<List<LearnersObject>> learnersObservable (Context application) {
        localRepository = new LoadLocalRepository(application);
        learnersObjects = repository.invokeLearnersList(localRepository);
        return Observable.just(learnersObjects);
    }

}
