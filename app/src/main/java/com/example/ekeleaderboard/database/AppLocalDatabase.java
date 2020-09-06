package com.example.ekeleaderboard.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.ekeleaderboard.dao.LearnersDao;
import com.example.ekeleaderboard.entity.LearningLeadersEntity;
import com.example.ekeleaderboard.entity.SkillIQLeadersEntity;
import com.example.ekeleaderboard.dao.SkillIqDao;

@Database(entities = {LearningLeadersEntity.class,
        SkillIQLeadersEntity.class}, version = 1)
public abstract class AppLocalDatabase extends RoomDatabase {

    private static volatile AppLocalDatabase INSTANCE;
    public abstract LearnersDao learnersDao();
    public abstract SkillIqDao skillIqDao();

    public static AppLocalDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppLocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppLocalDatabase.class, "AppLocalDb.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };


}
