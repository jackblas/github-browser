package com.jackblaszkowski.githubbrowser.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.jackblaszkowski.githubbrowser.database.converters.Converters;

@Database(entities = {GitHubRepoEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class GitHubRepoDatabase extends RoomDatabase {


    public abstract GitHubRepoDao repoDao();

    private static GitHubRepoDatabase INSTANCE;

    public static GitHubRepoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GitHubRepoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GitHubRepoDatabase.class, "githubbrowser_database")
                            // Wipe and rebuild
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

