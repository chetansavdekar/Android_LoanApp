package com.example.amol.loanquote;

/**
 * Created by amol13704 on 8/3/2017.
 */



        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;



public class FragmentUtils {

    private static FragmentUtils fragmentUtils;

    /**
     * create the singleton object of the FragmentUtils
     *
     * @return
     */
    public static FragmentUtils getInstance() {
        if (fragmentUtils == null) {
            fragmentUtils = new FragmentUtils();
        }
        return fragmentUtils;
    }

    private FragmentUtils() {

    }

    /**
     * clear the back stack of all the fragments from the fragment manager
     *
     * @param fragmentManager
     */
    public void clearBackStack(FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            try {
                //clear back stack
                fragmentManager.popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Method to add the fragment into the frament manager
     *
     * @param fragment        fragment which needs to be added
     * @param fragmentManager object of the fragmant manager class
     */
    /*public void addFragment(Fragment fragment, FragmentManager fragmentManager,
                            boolean backStack) {
        if (fragment == null) {
            return; //commit
        }

        String name = fragment.getClass().getSimpleName();
        //get the visible fragment
        Fragment visibleFragment = fragmentManager
                .findFragmentById(R.id.contentFrame);
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        if (visibleFragment != null && fragmentManager.getBackStackEntryCount() > 0) {

            visibleFragment.onPause(); // Call pause before hiding
            fragmentTransaction.hide(visibleFragment);
        }

        //add the new fragment onto the fragment manager
        fragmentTransaction.add(R.id.contentFrame, fragment, name);
        if (backStack) {
            fragmentTransaction.addToBackStack(name);
        }
        try {
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    public void addFragment(Fragment fragment, FragmentManager fragmentManager,
                            boolean backStack) {

        if (fragment == null) {
            return;
        }

        // Works with either the framework FragmentManager or the
// support package FragmentManager (getSupportFragmentManager).
        if(backStack) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, fragment)
                    // Add this transaction to the back stack
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, fragment, fragment.getClass().getSimpleName())
                    // Add this transaction to the back stack
                    .commit();
        }
    }


    /**
     * get the fragment manager backstack count
     */
    public int getBackStackEntryCount(FragmentManager fragmentManager) {

        return fragmentManager.getBackStackEntryCount();
    }

    /**
     * get the fragment from fragment manager using given tag
     */
    public Fragment getFragmentByTag(String tag, FragmentManager fragmentManager) {

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        return fragment;
    }


}

