package com.r0adkll.lostanimals.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.IonFormMultipartBodyRequestBuilder;
import com.r0adkll.deadskunk.utils.Utils;
import com.r0adkll.lostanimals.R;
import com.r0adkll.lostanimals.server.APIClient;
import com.r0adkll.lostanimals.server.model.Pet;
import com.r0adkll.lostanimals.utils.SmoothSeekBarChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by r0adkll on 6/2/13.
 */
public class ReportFragment extends Fragment implements LocationListener{
    private static final String TAG = "REPORT_PET";
    private static final int REQ_CODE_PICK_IMAGE = 1001;

    /*****************************************************
     * Static Initializer
     */

    public static ReportFragment createInstance(){
        ReportFragment frag = new ReportFragment();
        return frag;
    }

    /*****************************************************
     * Variables
     */
    private LocationManager locMan;

    /* Views */
    private ImageView thumbnail;
    private Switch reportTypeSwitch;
    private TextView reportTypeTitle;
    private SeekBar animalTypeSeek;
    private SeekBar sexTypeSeek;
    private EditText breed, color, size, name, desc;
    private String thumbnailPath = "";
    private double lat, lng;

    public ReportFragment(){}

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
        locMan = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        /* Load views from layout */
        thumbnail = findImage(R.id.thumbnail);
        reportTypeSwitch = (Switch) findView(R.id.report_type_switch);
        reportTypeTitle = findText(R.id.report_type_title);
        animalTypeSeek = findSeek(R.id.animal_type_seekbar);
        sexTypeSeek = findSeek(R.id.sex_type_seekbar);
        breed = findET(R.id.et_report_breed);
        color = findET(R.id.et_report_color);
        size = findET(R.id.et_report_size);
        name = findET(R.id.et_report_name);
        desc = findET(R.id.et_report_description);

        // Register View listeners
        sexTypeSeek.setOnSeekBarChangeListener(new SmoothSeekBarChangeListener(100));
        animalTypeSeek.setOnSeekBarChangeListener(new SmoothSeekBarChangeListener(100));

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Gallery to pick an image
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);
            }
        });

        reportTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    reportTypeTitle.setText("I found a pet.");
                }else{
                    reportTypeTitle.setText("I lost my pet.");
                }
            }
        });

        // Request User Coordinates
        locMan.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.getMainLooper());
        locMan.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, Looper.getMainLooper());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQ_CODE_PICK_IMAGE:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    thumbnailPath = filePath;

                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

                    // load image into thumbnail
                    thumbnail.setImageBitmap(yourSelectedImage);
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_report, parent, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.fragment_report, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
            case R.id.menu_send:
                // Pull data from all form fields
                String status = reportTypeSwitch.isActivated() ? "Found" : "Lost";
                String type = "other";
                int rawType  = (animalTypeSeek.getProgress() / 100);
                switch(rawType){
                    case 0:
                        type = "Dog";
                        break;
                    case 1:
                        type = "Cat";
                        break;
                    case 2:
                        type = "Other";
                        break;
                }

                String sex = "Unknown";
                int rawSex = (sexTypeSeek.getProgress() / 100);
                switch(rawSex){
                    case 0:
                        sex = "Male";
                        break;
                    case 1:
                        sex = "Female";
                        break;
                    case 2:
                        sex = "Unknown";
                        break;
                }

                String _breed = breed.getText().toString();
                String _color = color.getText().toString();
                String _size = size.getText().toString();
                String _name = size.getText().toString();
                String _desc = desc.getText().toString();
                String _lat = String.valueOf(lat);
                String _long = String.valueOf(lng);

                // Verify output data
                if(!status.isEmpty() && !_color.isEmpty() && !_size.isEmpty() && !_desc.isEmpty()){

                    IonFormMultipartBodyRequestBuilder mBuilder = Ion.with(getActivity())
                       .load(APIClient.getAPIUrl(APIClient.Targets.LIST_PETS))
                       .setMultipartParameter("pet[status]", status)
                       .setMultipartParameter("pet[color]", _color)
                       .setMultipartParameter("pet[size]", _size)
                       .setMultipartParameter("pet[description]", _desc)
                       .setMultipartParameter("pet[breed]", _breed)
                       .setMultipartParameter("pet[name]", _name)
                       .setMultipartParameter("pet[animal_type]", type)
                       .setMultipartParameter("pet[sex]", sex)
                       .setMultipartParameter("pet[last_seen_location_lat]", _lat)
                       .setMultipartParameter("pet[last_seen_location_long]", _long);

                        File file = new File(thumbnailPath);
                        if(file.exists() && !thumbnailPath.isEmpty())
                            mBuilder = mBuilder.setMultipartFile("pet[picture]", new File(thumbnailPath));



                       mBuilder.asJSONObject()
                       .setCallback(new FutureCallback<JSONObject>() {
                           @Override
                           public void onCompleted(Exception e, JSONObject result) {
                                if(result != null){
                                    try {
                                        Utils.log(TAG, "Report Animal Success: \n" + result.toString(2));

                                        // Create PET object and open detail view
                                        Pet pet = new Pet(result);

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }else{
                                    String msg = (e == null) ? "Error!" : e.getLocalizedMessage();
                                    Utils.log(TAG, "Report animal failed: " + msg);
                                }
                           }
                       });


                }

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
        // Remove location listeners
        locMan.removeUpdates(this);

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

    private SeekBar findSeek(int id){
        return (SeekBar) findView(id);
    }

    private EditText findET(int id){
        return (EditText) findView(id);
    }





    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

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
     * Inner Classes & Interfaces
     */

}
