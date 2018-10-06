package com.jackblaszkowski.githubbrowser.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "repository_table")
public class GitHubRepoEntity {

    @PrimaryKey
    @NonNull
    @SerializedName("node_id")
    private String id;

    @Expose(serialize = false, deserialize = false)
    private String login;

    @SerializedName("name")
    private String name;
    @SerializedName("html_url")
    private String htmlUrl;
    @SerializedName("description")
    private String description;
    @SerializedName("language")
    private String language;
    @SerializedName("stargazers_count")
    private int stargazersCount;

    @SerializedName("watchers")
    private int watchers;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;

    @Ignore
    public int count;

    @Ignore
    @SerializedName("owner")
    private Owner owner;


    public class Owner {
        @SerializedName("login")
        String login;

    }

    public GitHubRepoEntity(String id,
                            String login,
                            String name,
                            String htmlUrl,
                            String description,
                            String language,
                            int stargazersCount,
                            int watchers,
                            Date createdAt,
                            Date updatedAt) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.htmlUrl = htmlUrl;
        this.description = description;
        this.language = language;
        this.stargazersCount = stargazersCount;
        this.watchers = watchers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id;  }

    public String getLogin() {
        if(login==null) {
            return owner.login;
        } else {
            return login;
        }
    }

    public String getName() {
        return name;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public int getWatchers() {
        return watchers;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setLogin(String login) {

        this.login = login;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
