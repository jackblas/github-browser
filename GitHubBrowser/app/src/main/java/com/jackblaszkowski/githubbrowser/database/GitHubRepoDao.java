package com.jackblaszkowski.githubbrowser.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;

@Dao
public interface GitHubRepoDao {

    // Get all repositories for the selected user
    // Repositories are grouped by language. Languages with the most repositories are listed first.
    // Within each language group, repositories are sorted in descending order by the stargazers count.
    @Query("SELECT rt.id, rt.name, rt.login, rt.language, rt.stargazersCount, rc.count" +
            ", rt.watchers, rt.createdAt, rt.updatedAt" +
            " FROM repository_table rt  " +
            "left JOIN (Select language, COUNT(*) as count From repository_table " +
            "WHERE upper(login) = upper(:userLogin) GROUP BY language) rc " +
            "on rc.language = rt.language " +
            "WHERE upper(rt.login) = upper(:userLogin) " +
            "order by count desc, rt.language, stargazersCount desc")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    LiveData<List<GitHubRepoEntity>> loadAllRepositoriesForLogin(String userLogin);

    // Get details for the selected repository
    @Query("SELECT * from repository_table WHERE id = :recordId")
    LiveData<GitHubRepoEntity> loadDetailsForRepository(String recordId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GitHubRepoEntity repo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GitHubRepoEntity> repos);

    // Delete records for the selected user
    @Query("DELETE FROM repository_table WHERE login = :userLogin")
    void delete(String userLogin);

    @Query("DELETE FROM repository_table")
    void deleteAll();
}