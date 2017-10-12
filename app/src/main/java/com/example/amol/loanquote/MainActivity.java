package com.example.amol.loanquote;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.amol.fragment.ScreenFiveFragment;
import com.example.amol.fragment.ScreenFourFragment;
import com.example.amol.fragment.ScreenOneFragment;
import com.example.amol.fragment.ScreenThreeFragment;

public class MainActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();

        drawerLayout.closeDrawers();

        FragmentUtils.getInstance()
                .addFragment(ScreenOneFragment.newInstance(),
                        getSupportFragmentManager(), Boolean.FALSE);
    }

    @Override
    public void onBackPressed() {


        Fragment visibleFragment = getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if(visibleFragment instanceof ScreenThreeFragment
                || visibleFragment instanceof ScreenFourFragment
                || visibleFragment instanceof ScreenFiveFragment){
            getSupportFragmentManager().beginTransaction().remove(visibleFragment).commit();
        }
        super.onBackPressed();
    }
}













