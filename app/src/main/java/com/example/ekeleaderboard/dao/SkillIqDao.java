package com.example.ekeleaderboard.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ekeleaderboard.entity.SkillIQLeadersEntity;

import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface SkillIqDao {

    @Query("SELECT COUNT(*) FROM skilliq_table")
    int getLocalSkillIqCount();

    @Query("SELECT * FROM skilliq_table")
    Flowable<List<SkillIQLeadersEntity>> getLocalSkillIqCatalog();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLocalSkillIq(SkillIQLeadersEntity skillIQLeadersEntity);

    @Update
    void updateLocalSkillIq(SkillIQLeadersEntity... skillIQLeadersEntity);

    @Query("DELETE FROM skilliq_table")
    void deleteSkillIqTable();

}
