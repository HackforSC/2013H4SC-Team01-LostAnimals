package com.r0adkll.lostanimals.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.r0adkll.lostanimals.R;
import com.r0adkll.lostanimals.server.model.Pet;

import java.util.List;

/**
 * Created by r0adkll on 6/2/13.
 */
public class ComCenter {
    private static final String TAG = "COM_CENTER";
    private static final int MULTIPLE_PET_NOTIF_CODE = 11;

    /**
     * Singleton
     */

    private static ComCenter instance;

    /**
     * Singleton accessor
     */
    public static ComCenter getInstance(Context ctx){
        if(instance == null)
            instance = new ComCenter(ctx);

        return instance;
    }

    /**
     * Variables
     */
    Context ctx;
    NotificationManager manager;

    /**
     * Constructor
     * @param ctx
     */
    public ComCenter(Context ctx){
        manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        this.ctx = ctx;
    }

    public void createPetUpdateNotification(List<Pet> updatedPets, final String title, final String contentText){
        if(!updatedPets.isEmpty()){
            // Create the builder
            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(contentText);

            if(updatedPets.size() == 1){
                final Pet pet = updatedPets.get(0);

                // Create BigPictureNotification
                final NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();

                // load the image
                Ion.with(ctx)
                   .load(pet.picture)
                   .asBitmap()
                   .setCallback(new FutureCallback<Bitmap>() {
                       @Override
                       public void onCompleted(Exception e, Bitmap result) {
                           if(result != null){
                               style.bigPicture(result);
                               style.setBigContentTitle(title);
                               style.setSummaryText(contentText);

                               mBuilder.setStyle(style);

                               // Post Notification
                               manager.notify(pet.id, mBuilder.build());
                           }
                       }
                   });

            }else{

                final NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
                style.setBigContentTitle(updatedPets.size() + " pet statuses have updated");

                for(Pet pet: updatedPets){
                    style.addLine(pet.animal_type + " - " + pet.breed + " - " + pet.color);
                }

                mBuilder.setStyle(style);

                // Post Notification
                manager.notify(MULTIPLE_PET_NOTIF_CODE, mBuilder.build());
            }
        }
    }

}
