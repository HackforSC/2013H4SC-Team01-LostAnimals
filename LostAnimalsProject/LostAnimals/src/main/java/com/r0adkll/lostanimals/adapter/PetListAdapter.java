package com.r0adkll.lostanimals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cuubonandroid.sugaredlistanimations.GPlusListAdapter;
import com.cuubonandroid.sugaredlistanimations.SpeedScrollListener;
import com.koushikdutta.ion.Ion;
import com.r0adkll.deadskunk.utils.Utils;
import com.r0adkll.lostanimals.R;
import com.r0adkll.lostanimals.server.model.Pet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by r0adkll on 6/1/13.
 */
public class PetListAdapter extends GPlusListAdapter {

    /**
     * Variables
     */
    int viewResource;

    /**
     * Constructor
     *
     * @param context            application context
     * @param textViewResourceId the item view id
     * @param objects            the list of objects
     */
    public PetListAdapter(Context context, SpeedScrollListener speedListener, int textViewResourceId, List<? extends Object> objects) {
        super(context, speedListener, objects);
        viewResource = textViewResourceId;
    }

    /**
     * Variables
     */





    public ViewHolder createHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.petIcon = (ImageView) view.findViewById(R.id.pet_icon);
        holder.bannerImage = (ImageView) view.findViewById(R.id.banner_image);
        holder.date = (TextView) view.findViewById(R.id.tv_date);
        holder.description = (TextView) view.findViewById(R.id.description);
        holder.info = (ImageView) view.findViewById(R.id.btn_more_info);
        return holder;
    }


    public void bindHolder(ViewHolder holder, int position) {
        Utils.log("PLA", "bindHolder(" + position + ")");

        Pet data = (Pet) getItem(position);

        // Bind data to the view
        if(data.animal_type.equalsIgnoreCase("dog")){
            // Animal is a dog, set pet icon to dog icon
            holder.petIcon.setImageResource(R.drawable.ic_dog);
        }else if(data.animal_type.equalsIgnoreCase("cat")){
            // Animal is a cat, set pet icon to cat icon
            holder.petIcon.setImageResource(R.drawable.ic_cat);
        }

        // Use ION to load and cache the banner image
        Ion.with(holder.bannerImage)
           .placeholder(R.drawable.ic_action_person_light)
           .error(R.drawable.ic_action_about_light)
           .load(data.picture);

        // Set teh Description
        holder.description.setText(data.breed + " | " + data.color);

        // Set the time
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

        SimpleDateFormat outFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date time = format.parse(data.created_at);
            String date = outFormat.format(time);
            holder.date.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected View getRowView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(viewResource, parent, false);

            holder = createHolder(convertView);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        // Bind holder
        bindHolder(holder, position);

        return convertView;
    }


    static class ViewHolder {
        ImageView petIcon;
        ImageView bannerImage;
        TextView date;
        TextView description;
        ImageView info;
    }

}
