package com.jackblaszkowski.githubbrowser.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.jackblaszkowski.githubbrowser.database.GitHubRepoEntity;
import com.jackblaszkowski.githubbrowser.repository.AppDataRepository;
import com.jackblaszkowski.githubbrowser.repository.Resource;

import java.util.List;

public class RepoListViewModel extends AndroidViewModel {

    private AppDataRepository mRepository;

    private MutableLiveData<String> loginId = new MutableLiveData<>();
    private LiveData<Resource<List<GitHubRepoEntity>>> mAllRepos;

    public RepoListViewModel(Application application) {
        super(application);
        mRepository = new AppDataRepository(application);

        mAllRepos = Transformations.switchMap(loginId, (String login) -> {
            return mRepository.getAllRepositories(login);
        });
    }


    public void setLoginId(String login){
        loginId.setValue(login);
    }

    public LiveData<Resource<List<GitHubRepoEntity>>> getRepos(){
        return mAllRepos;
    }

}
