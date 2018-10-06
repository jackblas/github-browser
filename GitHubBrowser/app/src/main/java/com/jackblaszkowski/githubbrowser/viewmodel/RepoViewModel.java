package com.jackblaszkowski.githubbrowser.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.jackblaszkowski.githubbrowser.database.GitHubRepoEntity;
import com.jackblaszkowski.githubbrowser.repository.AppDataRepository;

public class RepoViewModel extends AndroidViewModel {

    private AppDataRepository mRepository;

    private MutableLiveData<String> recordId = new MutableLiveData<>();
    private LiveData<GitHubRepoEntity> mGithubRepo;
    private String mRepoRecordId;

    public RepoViewModel(Application application) {
        super(application);
        mRepository = new AppDataRepository(application);

        mGithubRepo = Transformations.switchMap(recordId, (String id) -> {
            return mRepository.getRepository(id);
        });
    }

    public LiveData<GitHubRepoEntity> getRepo() { return mGithubRepo; }

    public void setRecordId(String id){
        recordId.setValue(id);
    }


}
