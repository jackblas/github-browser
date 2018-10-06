package com.jackblaszkowski.githubbrowser.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jackblaszkowski.githubbrowser.R;

public class DetailsActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(savedInstanceState == null) {

            String recordId  = getIntent().getStringExtra(DetailsFragment.ARG_PARAM1);

            DetailsFragment fragment = DetailsFragment.newInstance(recordId);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_details_container, fragment)
                    .commit();
        }
    }

}
