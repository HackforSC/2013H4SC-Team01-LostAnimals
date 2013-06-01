package com.r0adkll.lostanimals.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by r0adkll on 6/1/13.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "TAG";

    /*****************************************************
     * Static Initializer
     */

    public static HomeFragment createInstance(){
        HomeFragment home = new HomeFragment();
        return home;
    }


    /*****************************************************
     * Variables
     */


    /**
     * Empty Constructor
     */
    public HomeFragment(){

    }

    /*****************************************************
     * Lifecycle Methods
     */

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){


        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*****************************************************
     * Helper Methods
     */



    /*****************************************************
     * Inner Classes & Interfaces
     */

}
