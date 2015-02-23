package com.example.ginji.rssfeed;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onUserLeaveHint()
    {
        android.support.v4.app.FragmentManager fm =  getSupportFragmentManager();
        NetworkFragment fragment_byTag =  (NetworkFragment) fm.findFragmentById(R.id.container);
        fragment_byTag.setItem();
        super.onUserLeaveHint();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NetworkFragment())
                    .commit();
        }
    }
}