package com.example.ekeleaderboard.repo;

import android.content.Context;

import com.example.ekeleaderboard.dao.LearnersDao;
import com.example.ekeleaderboard.dao.SkillIqDao;
import com.example.ekeleaderboard.database.AppLocalDatabase;
import com.example.ekeleaderboard.entity.LearningLeadersEntity;
import com.example.ekeleaderboard.entity.SkillIQLeadersEntity;

import java.util.List;
import io.reactivex.Flowable;

public class LoadLocalRepository {

    private LearnersDao learnersDao;
    private SkillIqDao skillIqDao;

    public Flowable<List<LearningLeadersEntity>> learnListFlowable;
    public Flowable<List<SkillIQLeadersEntity>> skillListFlowable;

    public LoadLocalRepository(Context application) {
        AppLocalDatabase localDb = AppLocalDatabase.getInstance(application);

        // Link learners table to app
        learnersDao = localDb.learnersDao();
        learnListFlowable = learnersDao.getLocalLearnersCatalog();

        // Link skillIq table to app
        skillIqDao = localDb.skillIqDao();
        skillListFlowable = skillIqDao.getLocalSkillIqCatalog();
    }

    // Room executes all queries on a separate thread.
    // Observed Observables will notify the observer when the data has changed.
    public Flowable<List<LearningLeadersEntity>> getAllLearnersCatalog() {
        return learnListFlowable;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public long insertLocalLearners(LearningLeadersEntity leadersEntity) {
        return learnersDao.insertLocalLearners(leadersEntity);
    }

    // Delete all data in local learners table
    public void deleteAllLearners(){
        learnersDao.deleteLearnersTable();
    }

    // Get skill from table
    public Flowable<List<SkillIQLeadersEntity>> getAllLocalSkillCatalog() {
        return skillListFlowable;
    }

    // Insert skill into table
    public long insertLocalSkillIq(SkillIQLeadersEntity skillIQLeadersEntity) {
        return skillIqDao.insertLocalSkillIq(skillIQLeadersEntity);
    }

    // Delete all data in local skillIq table
    public void deleteAllAudio(){
        skillIqDao.deleteSkillIqTable();
    }

}
