package com.jackblaszkowski.githubbrowser.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jackblaszkowski.githubbrowser.R;
import com.jackblaszkowski.githubbrowser.api.GitHubClient;
import com.jackblaszkowski.githubbrowser.database.GitHubRepoDao;
import com.jackblaszkowski.githubbrowser.database.GitHubRepoDatabase;
import com.jackblaszkowski.githubbrowser.database.GitHubRepoEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppDataRepository {

    private final String LOG_TAG = "AppDataRepository";

    private GitHubRepoDatabase mDb;
    private GitHubRepoDao mRepoDao;
    private LiveData<List<GitHubRepoEntity>> mAllRepos;

    private ConnectivityManager mConnMgr;
    private Resources mResources;

    private MediatorLiveData<Resource<List<GitHubRepoEntity>>> resourceMediatorLiveData;

    public AppDataRepository(Application application) {

        mDb = GitHubRepoDatabase.getDatabase(application);
        mRepoDao = mDb.repoDao();

        mConnMgr = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        mResources = application.getResources();

    }

    public LiveData<Resource<List<GitHubRepoEntity>>> getAllRepositories(String loginId) {

        resourceMediatorLiveData = new MediatorLiveData<>();
        mAllRepos = mRepoDao.loadAllRepositoriesForLogin(loginId);

        resourceMediatorLiveData.addSource(mAllRepos, new Observer<List<GitHubRepoEntity>>() {
            @Override
            public void onChanged(@Nullable List<GitHubRepoEntity> gitHubRepoEntities) {
                Resource<List<GitHubRepoEntity>> resource = Resource.success(mAllRepos.getValue());
                resourceMediatorLiveData.setValue(resource);
            }
        });

        refresh(loginId);

        return resourceMediatorLiveData;
    }

    // All details info is already in the db. No need for an API call.
    public LiveData<GitHubRepoEntity> getRepository(final String recordId) {

        return mDb.repoDao().loadDetailsForRepository(recordId);
    }


    public void insert (GitHubRepoEntity repo) {
        insertOne(repo);
    }

    private void refresh(String loginId){

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GitHubClient client = retrofit.create(GitHubClient.class);
        Call<List<GitHubRepoEntity>> call = client.listRepos(loginId);

        call.enqueue(new Callback<List<GitHubRepoEntity>>() {
            @Override
            public void onResponse(Call<List<GitHubRepoEntity>> call, Response<List<GitHubRepoEntity>> response) {
                Log.d(LOG_TAG, "Call:" + response.toString());

                if(response.isSuccessful()){
                    List<GitHubRepoEntity> repos = response.body();
                    insertAll(repos);
                } else {
                    resourceMediatorLiveData.setValue(Resource.error(response.message(),mAllRepos.getValue()));
                }

            }

            @Override
            public void onFailure(Call<List<GitHubRepoEntity>> call, Throwable t) {
                if(!isOnline()){
                    Log.d(LOG_TAG,"In the onFailure(): Device is offline");
                    resourceMediatorLiveData.setValue(Resource.error(mResources.getString(R.string.no_connection_message),mAllRepos.getValue()));
                } else {
                    Log.e(LOG_TAG,"In the onFailure(): " + t.getMessage());
                    resourceMediatorLiveData.setValue(Resource.error(t.getMessage(),mAllRepos.getValue()));
                }

            }
        });

    }

    private void insertOne(final GitHubRepoEntity repository) {
        Log.v(LOG_TAG, "In insertOne()");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            executor.execute(new Thread() {
                @Override
                public void run() {
                    mRepoDao.insert(repository);
                }
            });

        } finally {
            executor.shutdown();
        }

    }

    private void insertAll(final List<GitHubRepoEntity> repositories) {
        Log.v(LOG_TAG, "In insertAll()");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            executor.execute(new Thread() {
                @Override
                public void run() {
                    mRepoDao.insertAll(repositories);
                }
            });

        } finally {
            executor.shutdown();
        }

    }

    private boolean isOnline() {

        NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

}
