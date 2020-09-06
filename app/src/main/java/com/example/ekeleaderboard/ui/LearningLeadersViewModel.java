package com.example.ekeleaderboard.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ekeleaderboard.entity.LearningLeadersEntity;
import com.example.ekeleaderboard.repo.LoadLocalRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;


public class LearningLeadersViewModel extends AndroidViewModel {

    LoadLocalRepository localRepository;
    private CompositeDisposable disposable;
    private MutableLiveData<List<LearningLeadersEntity>> localLearnData = new MutableLiveData<>();
    private MutableLiveData<Boolean> localLearnError = new MutableLiveData<>();
    private MutableLiveData<Boolean> localLearnLoading = new MutableLiveData<>();


    public LearningLeadersViewModel(@NonNull Application application) {
        super(application);
        disposable = new CompositeDisposable();
        localRepository = new LoadLocalRepository(getApplication());
        fetchLocalLearners();
    }

    public LiveData<List<LearningLeadersEntity>> getLocalLearnRepos() {
        return localLearnData;
    }


    public LiveData<Boolean> getLocalLearnError() {
        return localLearnError;
    }


    public LiveData<Boolean> getLocalLearnLoading() {
        return localLearnLoading;
    }

    public Flowable<List<LearningLeadersEntity>> getLearners(){
        return new LoadLocalRepository(getApplication()).learnListFlowable;
    }

    public void fetchLocalLearners() {
        localLearnLoading.setValue(true);
        disposable.add(getLearners()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<LearningLeadersEntity>>() {

                    @Override
                    public void onNext(List<LearningLeadersEntity> leadersEntities) {
                        if(leadersEntities != null) {
                            Collections.sort(leadersEntities, new Comparator<LearningLeadersEntity>() {
                                @Override
                                public int compare(LearningLeadersEntity localAudioEntity, LearningLeadersEntity textSort) {
                                    return localAudioEntity.getName().compareTo(textSort.getName());
                                }
                            });

                            localLearnError.setValue(false);
                            localLearnData.setValue(leadersEntities);
                            localLearnLoading.setValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        localLearnError.setValue(true);
                        localLearnLoading.setValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }

                }));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }

    }


}