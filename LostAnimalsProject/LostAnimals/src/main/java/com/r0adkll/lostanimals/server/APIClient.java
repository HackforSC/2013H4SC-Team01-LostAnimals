package com.r0adkll.lostanimals.server;

import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by r0adkll on 6/1/13.
 */
public class APIClient {

    /**
     * API URL Constants
     */

    public static final String BASE = "http://lostanimals.herokuapp.com/";

    /**
     * Form a full API url to request from
     * @param target
     * @return
     */
    public static String getAPIUrl(String target){
        return BASE.concat(target);
    }

    /**
     * Form a full API url to request from
     * @param target
     * @return
     */
    public static String getAPIUrlWithParams(String target, HashMap<String, String> params){
        return BASE.concat(target).concat(encodeParams(params));
    }

    /**
     * Encode a hashmap to URL parameters
     * @param params
     * @return
     */
    public static String encodeParams(HashMap<String, String> params){
        String param = "?";
        List<String> keys = new ArrayList<String>(params.keySet());
        List<String> values = new ArrayList<String>(params.values());

        int size = keys.size();
        for(int i=0; i<size; i++){
            String key = keys.get(i);
            String value = values.get(i);
            String part = key + "=" + value;

            param = param.concat(part);
            if(i != size-1){
                param = param.concat("&");
            }
        }
        return param;
    }


    /**
     * API Targets
     */
    public static class Targets{
        public static final String LIST_PETS = "pets.json";
    }

}
