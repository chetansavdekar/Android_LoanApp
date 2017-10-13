package com.example.amol.loanquote;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by amol13704 on 8/3/2017. //commit
 */

public class FragmentInteractor {

    private FragmentInteractor fragmentInteractor;
    private Context context;

    public android.support.v4.app.FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(android.support.v4.app.FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private android.support.v4.app.FragmentManager fragmentManager;

    public FragmentInteractor getFragmentInteractor(Context context){
        if(fragmentInteractor == null){
            fragmentInteractor = new FragmentInteractor();
            return fragmentInteractor;
        } else {
            return fragmentInteractor;
        }
    }

    public void navigateToFragment(Fragment fragmentFrom, Fragment fragmentTo, Bundle bundle){

    }

    public void inflateFragment(Fragment fragment){

    }

}
