package com.jackblaszkowski.githubbrowser.api;

import com.jackblaszkowski.githubbrowser.database.GitHubRepoEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubClient {
    //https://api.github.com/users/{user}/repos
    @GET("users/{user}/repos")
    Call<List<GitHubRepoEntity>> listRepos(@Path("user") String user);

}
