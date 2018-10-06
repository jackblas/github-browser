package com.jackblaszkowski.githubbrowser.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackblaszkowski.githubbrowser.R;
import com.jackblaszkowski.githubbrowser.database.GitHubRepoEntity;
import com.jackblaszkowski.githubbrowser.viewmodel.RepoViewModel;

import java.text.DateFormat;


public class DetailsFragment extends Fragment {

    static final String ARG_PARAM1 = "param1";
    private String mRecordId;
    private RepoViewModel mRepoViewModel;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(String param1) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecordId = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        mRepoViewModel = ViewModelProviders.of(this).get(RepoViewModel.class);
        mRepoViewModel.setRecordId(mRecordId);

        mRepoViewModel.getRepo().observe(this, new Observer<GitHubRepoEntity>() {
            @Override
            public void onChanged(@Nullable final GitHubRepoEntity repo) {

                TextView nameView=rootView.findViewById(R.id.repo_details_name);
                String fullName = getResources().getString(R.string.full_name_formatted, repo.getLogin(), repo.getName());
                nameView.setText(fullName);

                TextView descriptionView=rootView.findViewById(R.id.repo_details_description);
                descriptionView.setText(repo.getDescription());

                TextView languageView=rootView.findViewById(R.id.repo_details_language);
                languageView.setText(repo.getLanguage());

                TextView starCountView=rootView.findViewById(R.id.repo_details_star_count);
                starCountView.setText(Integer.toString(repo.getStargazersCount()));

                TextView watchersView=rootView.findViewById(R.id.repo_details_watchers);
                watchersView.setText(Integer.toString(repo.getWatchers()));

                DateFormat dateFormat = DateFormat.getDateInstance();
                TextView createdAtView=rootView.findViewById(R.id.repo_details_created_at);
                createdAtView.setText(dateFormat.format(repo.getCreatedAt()));
                TextView updatedAtView=rootView.findViewById(R.id.repo_details_updated_at);
                updatedAtView.setText(dateFormat.format(repo.getUpdatedAt()));


            }
        });

        return rootView;
    }

}
