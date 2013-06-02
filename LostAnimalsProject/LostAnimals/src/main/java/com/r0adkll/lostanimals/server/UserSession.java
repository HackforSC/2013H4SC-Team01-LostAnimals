package com.r0adkll.lostanimals.server;

import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.r0adkll.lostanimals.server.model.Pet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by r0adkll on 6/1/13.
 */
public class UserSession {

    /***********************
     * Singleton methods
     */

    private static UserSession instance;

    /**
     * Singleton accessor method
     * @return  the UserSession singleton
     */
    public static UserSession getSession(){
        if(instance == null){
            instance = new UserSession();
        }
        return instance;
    }


    /************************
     * Variables
     */

    // this is the current list of pets being tracked
    List<Pet> petList;


    /*************************
     * Network request methods
     */

    /**
     * Get a list of currently loaded pet data
     */
    public List<Pet> getCurrentPetList(){
        return petList;
    }

    /**
     * Load Pet data from the server
     * @param ctx       context reference
     * @param handler   the request callback handler
     */
    public void loadPetData(Context ctx,HashMap<String, String> params,  final OnPetDataLoad handler){
        // Create new list
        petList = new ArrayList<Pet>();

        // make request to get list of pets from api
        // using Ion, make network request
        Ion.Config cfg;

        Ion.with(ctx)
                .load(APIClient.getAPIUrlWithParams(APIClient.Targets.LIST_PETS, params))
                .asJSONArray()
                .setCallback(new FutureCallback<JSONArray>() {
                    @Override
                    public void onCompleted(Exception e, JSONArray result) {
                        if (result != null) {
                            // Get the list of JSON objects
                            int size = result.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject jObj = result.optJSONObject(i);

                                // Create Pet Objects and dd to the list
                                Pet newPet = new Pet(jObj);
                                petList.add(newPet);

                                // Call callback
                                handler.onSuccess(petList);
                            }
                        } else {
                            String msg = "JSONArray is null";
                            if (e != null) msg = e.getLocalizedMessage();
                            handler.onFailure(msg);
                        }
                    }
                });

    }


    /****************************
     * Interfaces
     */

    /**
     * Pet Load listener callback
     */
    public static interface OnPetDataLoad{
        public void onSuccess(List<Pet> pets);
        public void onFailure(String message);
    }


}
