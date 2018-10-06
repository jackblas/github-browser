package com.jackblaszkowski.githubbrowser.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackblaszkowski.githubbrowser.R;
import com.jackblaszkowski.githubbrowser.database.GitHubRepoEntity;
import com.jackblaszkowski.githubbrowser.repository.Resource;
import com.jackblaszkowski.githubbrowser.viewmodel.RepoListViewModel;

import java.util.List;


public class MainActivityFragment extends Fragment {

    static final String ARG_PARAM1 = "param1";
    private String mLoginId;

    private RepoListViewModel mRepoListViewModel;
    private RepoListAdapter adapter;
    private View mRootView;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance(String param1) {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLoginId = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mRootView = inflater.inflate(com.jackblaszkowski.githubbrowser.R.layout.fragment_main, container, false);
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerview);

        adapter = new RepoListAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRepoListViewModel= ViewModelProviders.of(getActivity()).get(RepoListViewModel.class);

        if(mLoginId != null) {

            mRepoListViewModel.setLoginId(mLoginId);
            mRepoListViewModel.getRepos().observe(this, new Observer<Resource<List<GitHubRepoEntity>>>() {
                @Override
                public void onChanged(@Nullable final Resource<List<GitHubRepoEntity>> resource) {

                    adapter.setRepos(resource.data);

                    if (resource.status != Resource.Status.SUCCESS) {
                        showErrorMessage(resource.message);
                    }
                }
            });
        }

    }

    private void showErrorMessage(String message) {
        Snackbar snackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
