package com.jackblaszkowski.githubbrowser.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jackblaszkowski.githubbrowser.R;
import com.jackblaszkowski.githubbrowser.database.GitHubRepoEntity;

import java.util.List;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {

    class RepoViewHolder extends RecyclerView.ViewHolder {
        private final TextView repoNameView;
        private final TextView repoLanguageView;
        private final TextView repoStargazersView;
        private final LinearLayout parentLayout;

        private RepoViewHolder(View itemView) {
            super(itemView);
            repoNameView = itemView.findViewById(R.id.repo_list_name);
            repoLanguageView = itemView.findViewById(R.id.repo_list_language);
            repoStargazersView = itemView.findViewById(R.id.repo_list_star_count);

            parentLayout = itemView.findViewById(R.id.list_item_layout);
        }
    }

    private final LayoutInflater mInflater;
    private List<GitHubRepoEntity> mRepos;

    RepoListAdapter(Context context) {
        mInflater = LayoutInflater.from(context); }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RepoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        final GitHubRepoEntity current = mRepos.get(position);

        holder.repoNameView.setText(current.getName());
        holder.repoLanguageView.setText(current.getLanguage());
        holder.repoStargazersView.setText(Integer.toString(current.getStargazersCount()));


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent intent = new Intent(context,DetailsActivity.class);
                intent.putExtra(DetailsFragment.ARG_PARAM1,current.getId());
                context.startActivity(intent);
            }
        });

    }

    void setRepos(List<GitHubRepoEntity> repos){
        mRepos = repos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (mRepos != null)
            return mRepos.size();
        else return 0;
    }
}
