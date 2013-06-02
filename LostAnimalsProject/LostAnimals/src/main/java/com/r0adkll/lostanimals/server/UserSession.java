package com.r0adkll.lostanimals.server;

import android.content.Context;
import android.util.SparseArray;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.r0adkll.deadskunk.utils.FileUtils;
import com.r0adkll.deadskunk.utils.Utils;
import com.r0adkll.lostanimals.server.model.Pet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by r0adkll on 6/1/13.
 */
public class UserSession {
    private static final String TAG = "USER_SESSION";

    /***********************
     * Singleton methods
     */

    private static UserSession instance;

    /**
     * Singleton accessor method
     * @return  the UserSession singleton
     */
    public static UserSession getSession(Context ctx){
        if(instance == null){
            instance = new UserSession(ctx);
        }
        return instance;
    }


    /************************
     * Variables
     */
    private Context ctx;

    // this is the current list of pets being tracked
    private List<Pet> petList;
    private List<PetChangeObserver> petChangeObservers = new ArrayList<PetChangeObserver>();

    private HashMap<Integer, Pet> petCacheMap = new HashMap<Integer, Pet>();
    private boolean isFirstLoad = true;


    public UserSession(Context ctx){
        this.ctx = ctx;

        // load pet cache from disk
        isFirstLoad = !loadPetCache();
    }

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
                            if(size == 0){
                                if(handler != null) handler.onSuccess(petList);
                                return;
                            }


                            for (int i = 0; i < size; i++) {
                                JSONObject jObj = result.optJSONObject(i);

                                // Create Pet Objects and dd to the list
                                Pet newPet = new Pet(jObj);
                                petList.add(newPet);
                            }

                            // Call callback
                            if(handler != null) handler.onSuccess(petList);

                            // Analyze new Pets
                            analyzeNewPet(petList);

                            // Cache Pet List
                            cachePetList(petList);

                            // Save Cache
                            savePetCache();

                        } else {
                            String msg = "JSONArray is null";
                            if (e != null) msg = e.getLocalizedMessage();
                            if(handler != null) handler.onFailure(msg);
                        }
                    }
                });

    }

    private boolean savePetCache(){
        /* METRIC */ long pre = System.currentTimeMillis();
        int result = FileUtils.writeObjectToInternal(ctx, "PET_SEEN_CACHE.cache", petCacheMap);
        /* METRIC */ long post = System.currentTimeMillis();
        Utils.log(TAG, "Pet cache saved in " + (post-pre) + " ms.");

        if(result == FileUtils.IO_SUCCESS) return true;
        else return false;
    }

    private boolean loadPetCache(){
        /* METRIC */ long pre = System.currentTimeMillis();
        HashMap<Integer, Pet> map = (HashMap<Integer, Pet>) FileUtils.readObject(ctx, "PET_SEEN_CACHE.cache");
        if(map != null){
            petCacheMap = new HashMap<Integer, Pet>(map);
            /* METRIC */ long post = System.currentTimeMillis();
            Utils.log(TAG, "Pet cache loaded in " + (post-pre) + " ms.");

            return true;
        }else{
            return false;
        }
    }

    private void cachePetList(List<Pet> pets){
        for(Pet pet: pets){
            petCacheMap.put(pet.id, pet);
        }
    }

    /**
     * Analyze new Pet updates
     * @param pets
     */
    private void analyzeNewPet(List<Pet> pets){
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        List<Pet> changeList = new ArrayList<Pet>();

        for(Pet pet: pets){

            // Attempt to find cache
            Pet cachePet = petCacheMap.get(pet.id);
            if(cachePet != null){
                // Convert into date objects
                try {
                    Date newPetDate = format.parse(pet.updated_at);
                    Date oldPetDate = format.parse(cachePet.updated_at);

                    if(newPetDate.after(oldPetDate)){
                        // UPDATE, Spawn notification
                        changeList.add(pet);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                    // Do Nothing
                }

            }else{
                changeList.add(pet);
            }
        }

        // dispatch pet change
        if(!changeList.isEmpty() && !isFirstLoad){
            dispatchPetChange(changeList);
        }
    }

    public void registerPetChangeObserver(PetChangeObserver observer){
        if(!petChangeObservers.contains(observer))
            petChangeObservers.add(observer);
    }

    public void unregisterPetChangeObserver(PetChangeObserver observer){
        petChangeObservers.remove(observer);
    }

    private void dispatchPetChange(List<Pet> pets){
        for(PetChangeObserver observer: petChangeObservers){
            observer.onPetsChange(pets);
        }
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

    public static interface PetChangeObserver{
        public void onPetsChange(List<Pet> pets);
    }


}
