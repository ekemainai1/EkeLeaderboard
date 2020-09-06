package com.example.ekeleaderboard.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ekeleaderboard.entity.LearningLeadersEntity;

import java.util.List;
import io.reactivex.Flowable;


@Dao
public interface LearnersDao {

    @Query("SELECT COUNT(*) FROM learners_table")
    int getLocalLearnersCount();

    @Query("SELECT * FROM learners_table")
    Flowable<List<LearningLeadersEntity>> getLocalLearnersCatalog();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLocalLearners(LearningLeadersEntity learningLeadersEntity);

    @Update
    void updateLocalLearners(LearningLeadersEntity... learningLeadersEntity);

    @Query("DELETE FROM learners_table")
    void deleteLearnersTable();

}
