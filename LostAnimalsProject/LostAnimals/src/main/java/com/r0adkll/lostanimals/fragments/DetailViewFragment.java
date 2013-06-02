package com.r0adkll.lostanimals.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.r0adkll.lostanimals.R;
import com.r0adkll.lostanimals.server.model.Pet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by r0adkll on 6/1/13.
 */
public class DetailViewFragment extends Fragment {
    private static final String TAG = "DETAIL_VIEW";

    /*****************************************************
     * Static Initializer
     */

    public static DetailViewFragment createInstance(Pet pet){
        DetailViewFragment frag = new DetailViewFragment();
        frag.setPet(pet);
        return frag;
    }


    /*****************************************************
     * Variables
     */

    // Generic Tools
    private final StyleSpan bss = new StyleSpan(Typeface.BOLD); // the span to bold parts of strings

    /* View Variables */
    private ImageView banner;
    private TextView type, color, sex, size, created, updated, status, description;

    /* Data */
    private Pet data;

    /**
     * Empty Constructor
     */
    public DetailViewFragment(){}

    /*****************************************************
     * Lifecycle Methods
     */

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        // Load Views from layout
        banner = findImage(R.id.banner);
        type = findText(R.id.tv_type);
        color = findText(R.id.tv_color);
        sex = findText(R.id.tv_sex);
        size = findText(R.id.tv_size);
        created = findText(R.id.tv_created_at);
        updated = findText(R.id.tv_updated_at);
        status = findText(R.id.tv_status);
        description = findText(R.id.tv_description);

        // Inflate pet data
        inflatePetData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_detailview, parent, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.fragment_detailview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                // Pop the backstack
                getFragmentManager().popBackStack();

                return true;
            case R.id.menu_report:

                // Show report Fragment
                ReportFragment report = ReportFragment.createInstance();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, report, "REPORT")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

                return true;
        }

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

    private View findView(int id){
        return getActivity().findViewById(id);
    }

    private ImageView findImage(int id){
        return (ImageView) findView(id);
    }

    private TextView findText(int id){
        return (TextView) findView(id);
    }

    /**
     * Set the pet data
     * @param pet
     */
    public void setPet(Pet pet){
        data = pet;
    }

    private void inflatePetData(){

        // Use ION to load image
        Ion.with(banner)
           .placeholder(R.drawable.ic_placeholder)
           .error(R.drawable.ic_cat)
           .load(data.picture);

        // Load features
        type.setText(boldFeature("Type: ", data.animal_type));
        color.setText(boldFeature("Color: ", data.color));
        sex.setText(boldFeature("Sex: ", data.sex));
        size.setText(boldFeature("Size: ", data.size));
        status.setText(data.status);
        description.setText(data.description);

        if(data.status.equalsIgnoreCase("lost")){
            status.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }else{
            status.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        }

        // Format dates// Set the time
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date createdTime = format.parse(data.created_at);
            String crTime = outFormat.format(createdTime);

            Date updatedTime = format.parse(data.updated_at);
            String upTime = outFormat.format(updatedTime);

            // Set views
            created.setText("Created on " + crTime);
            updated.setText("Updated on " + upTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    private SpannableStringBuilder boldFeature(String title, String message){
        String superStr = title.concat(message);



        final SpannableStringBuilder sb = new SpannableStringBuilder(superStr);

        // Apply span
        sb.setSpan(bss, 0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return sb;
    }

    /*****************************************************
     * Inner Classes & Interfaces
     */

}
