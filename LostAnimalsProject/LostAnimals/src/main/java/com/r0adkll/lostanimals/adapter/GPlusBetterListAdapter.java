package com.r0adkll.lostanimals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cuubonandroid.sugaredlistanimations.GPlusListAdapter;
import com.cuubonandroid.sugaredlistanimations.SpeedScrollListener;

import java.util.List;

/**
 * Optimized List adapter based on Android Effiecent List Adapter
 * sample (List14.java)
 * 
 * @author r0adkll
 *
 */
public abstract class GPlusBetterListAdapter extends GPlusListAdapter{

	/**
	 * Variables
	 */
	private int viewResource = -1;
	private LayoutInflater inflater;

	/**
	 * Constructor
	 * @param context					application context
	 * @param textViewResourceId		the item view id
	 * @param objects					the list of objects
	 */
	public GPlusBetterListAdapter(Context context, SpeedScrollListener speedListener, int textViewResourceId, List objects) {
		super(context, speedListener, objects);
		viewResource = textViewResourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * The data holder for the view
	 * 
	 * @author drew.heavner
	 */
	public static class ViewHolder{	}

    @Override
    protected View getRowView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){

            // Load the view from scratch
            convertView = inflater.inflate(viewResource, parent, false);

            // Load the ViewHolder
            holder = createHolder(convertView);

            // set holder to the tag
            convertView.setTag(holder);

        }else{

            // Pull the view holder from convertView's tag
            holder = (ViewHolder) convertView.getTag();

        }

        // bind the data to the holder
        bindHolder(holder, position);

        return convertView;
    }
	
	/**
	 * Create View/View holder
	 * 
	 * 	Here you load all your views from the layout into a custom 
	 *  view holder that you overrid from ViewHolder();
	 * 
	 * @param view
	 * @return
	 */
	public abstract ViewHolder createHolder(View view);
	
	/**
	 * Bind the Data from the data object to the view elements you
	 * created in 'createHolder()'
	 * 
	 * @param holder
	 * @param position
	 */
	public abstract void bindHolder(ViewHolder holder, int position);
	
}
