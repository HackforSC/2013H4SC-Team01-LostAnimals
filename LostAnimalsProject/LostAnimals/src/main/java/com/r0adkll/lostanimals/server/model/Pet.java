package com.r0adkll.lostanimals.server.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by r0adkll on 6/1/13.
 */
public class Pet implements Serializable{


    /**
     * Variables
     */
    public String animal_type;
    public String breed;
    public String color;
    public String created_at;
    public String cur_location_lat;
    public String cur_location_long;
    public String description;
    public String finder_id;
    public int id;
    public double last_seen_location_lat;
    public double last_seen_location_long;
    public String name;
    public String owner_id;
    public String picture;
    public String sex;
    public String size;
    public String status;
    public String updated_at;


    /**
     * JSON Constructor
     * @param json  the json object ot construct from
     */
    public Pet(JSONObject json){
        animal_type = json.optString("animal_type");
        breed = json.optString("breed");
        color = json.optString("color");
        created_at = json.optString("created_at");
        cur_location_lat = json.optString("cur_location_lat");
        cur_location_long = json.optString("cur_location_long");
        description = json.optString("description");
        finder_id = json.optString("finder_id");
        id = json.optInt("id");
        last_seen_location_lat = json.optDouble("last_seen_location_lat");
        last_seen_location_long = json.optDouble("last_seen_location_long");
        name = json.optString("name");
        owner_id = json.optString("owner_id");
        picture = json.optString("picture");
        sex = json.optString("sex");
        size = json.optString("size");
        status = json.optString("status");
        updated_at = json.optString("updated_at");
    }



}
