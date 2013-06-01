package com.r0adkll.lostanimals;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.r0adkll.lostanimals.fragments.HomeFragment;

public class LAMain extends Activity {

    /**
     * Variables
     */
    private HomeFragment home;


    /**
     * Create the app
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Load the Home Fragment
        if(savedInstanceState == null){
            // Create new home fragment and add it to the UI
            home = HomeFragment.createInstance();

            // Add via FragmentManager
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, home, "HOME")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        }else{
            // Load Home Fragment reference from the activity
            home = (HomeFragment) getFragmentManager().findFragmentByTag("HOME");
            // IF SO, do something here
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lamain, menu);
        return true;
    }
    
}
