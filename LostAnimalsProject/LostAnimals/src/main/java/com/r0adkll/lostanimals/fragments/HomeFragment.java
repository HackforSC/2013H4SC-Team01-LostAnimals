package com.r0adkll.lostanimals.fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cuubonandroid.sugaredlistanimations.SpeedScrollListener;
import com.r0adkll.deadskunk.utils.Utils;
import com.r0adkll.lostanimals.R;
import com.r0adkll.lostanimals.adapter.PetListAdapter;
import com.r0adkll.lostanimals.server.UserSession;
import com.r0adkll.lostanimals.server.model.Pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by r0adkll on 6/1/13.
 */
public class HomeFragment extends Fragment implements LocationListener{
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

    private LocationManager locMan;
    private SpeedScrollListener listener;

    /* Teh Main listview for this fragment */
    private ListView petList;
    private TextView noPetsMsg;
    private ProgressBar loading;

    private PetListAdapter adapter;
    private List<Pet> petData;



    /**
     * Empty Constructor
     */
    public HomeFragment(){}

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
        // Load Location Manager
        locMan = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Load LIst View
        listener = new SpeedScrollListener();
        petList = (ListView)getActivity().findViewById(R.id.home_list);
        noPetsMsg = (TextView)getActivity().findViewById(R.id.no_pets_message);
        loading = (ProgressBar)getActivity().findViewById(R.id.pb_loading);

        petList.setOnScrollListener(listener);

        // Init data array
        petData = new ArrayList<Pet>();

        // Create Adapter and bind to list view
        adapter = new PetListAdapter(getActivity().getApplicationContext(), listener, R.layout.layout_home_item, petData);
        petList.setAdapter(adapter);

        // Make location request
        locMan.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.getMainLooper());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_home, parent, false);
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
     * Location Listener Methdos
     */

    @Override
    public void onLocationChanged(Location location) {
        Utils.log(getTag(), "onLocationChanged(" + location.getLatitude() + ", " + location.getLongitude() + ", " + location.getAccuracy());

        final String RANGE = "50";

        // GPS Coord parameter variables
        final double lat = location.getLatitude();
        final double lng = location.getLongitude();

        // Encode Parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lat", String.valueOf(lat));
        params.put("long", String.valueOf(lng));
        params.put("range", RANGE);

        // Load Pet Data
        UserSession.getSession().loadPetData(getActivity(), params, new UserSession.OnPetDataLoad() {
            @Override
            public void onSuccess(List<Pet> pets) {
                Utils.log(TAG, "Loaded Pet Data: " + pets.size());

                // Hide the loading bar
                loading.setVisibility(View.GONE);

                // if the results are empty, show empty message
                if(pets.isEmpty()){
                    noPetsMsg.setVisibility(View.VISIBLE);
                }else{
                    noPetsMsg.setVisibility(View.GONE);
                }

                // Update pet data
                petData.clear();
                petData.addAll(pets);

                // Call change to the adapter
                adapter = new PetListAdapter(getActivity().getApplicationContext(), listener, R.layout.layout_home_item, petData);
                petList.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {
                // Hide the loading bar
                loading.setVisibility(View.GONE);

                // if the results are empty, show empty message
                if(petData.isEmpty()){
                    noPetsMsg.setVisibility(View.VISIBLE);
                }else{
                    noPetsMsg.setVisibility(View.GONE);
                }

                Utils.log(TAG, "Pet data failed to load with: " + message);
            }
        });

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /*****************************************************
     * Helper Methods
     */



    /*****************************************************
     * Inner Classes & Interfaces
     */

}
