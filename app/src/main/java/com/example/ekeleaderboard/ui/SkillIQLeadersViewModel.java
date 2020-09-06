package com.example.ekeleaderboard.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.ekeleaderboard.repo.LoadLocalRepository;
import com.example.ekeleaderboard.entity.SkillIQLeadersEntity;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SkillIQLeadersViewModel extends AndroidViewModel {

    LoadLocalRepository localRepository;
    private CompositeDisposable disposable;
    private MutableLiveData<List<SkillIQLeadersEntity>> localSkillData = new MutableLiveData<>();
    private MutableLiveData<Boolean> localSkillError = new MutableLiveData<>();
    private MutableLiveData<Boolean> localSkillLoading = new MutableLiveData<>();

    public SkillIQLeadersViewModel(@NonNull Application application) {
        super(application);
        disposable = new CompositeDisposable();
        localRepository = new LoadLocalRepository(getApplication());
        fetchLocalSkillIq();
    }

    public LiveData<List<SkillIQLeadersEntity>> getLocalSkillRepos() {
        return localSkillData;
    }


    public LiveData<Boolean> getLocalSkillError() {
        return localSkillError;
    }

    public LiveData<Boolean> getLocalSkillLoading() {
        return localSkillLoading;
    }

    public Flowable<List<SkillIQLeadersEntity>> getSkills(){
        return localRepository.skillListFlowable;
    }

    public void fetchLocalSkillIq() {
        localSkillLoading.setValue(true);
        disposable.add(getSkills()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<SkillIQLeadersEntity>>() {

                    @Override
                    public void onNext(List<SkillIQLeadersEntity> leadersEntities) {
                        if(leadersEntities != null) {
                            Collections.sort(leadersEntities, new Comparator<SkillIQLeadersEntity>() {
                                @Override
                                public int compare(SkillIQLeadersEntity iqLeadersEntity, SkillIQLeadersEntity textSort) {
                                    return iqLeadersEntity.getName().compareTo(textSort.getName());
                                }
                            });

                            localSkillError.setValue(false);
                            localSkillData.setValue(leadersEntities);
                            localSkillLoading.setValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        localSkillError.setValue(true);
                        localSkillLoading.setValue(false);
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