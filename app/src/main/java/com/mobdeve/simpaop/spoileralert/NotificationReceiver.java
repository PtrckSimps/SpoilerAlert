package com.mobdeve.simpaop.spoileralert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "SPOILERALERT";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        int ID = intent.getIntExtra("ID", 0);
        String name = intent.getStringExtra("NAME");
        String date = intent.getStringExtra("DATE");
        byte[] bytes = intent.getByteArrayExtra("IMAGE");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //notification attributes
        builder.setSmallIcon(R.drawable.sa_icon);
        builder.setContentTitle("SpoilerAlert");
        builder.setLargeIcon(bmp);
        builder.setContentText(name + " is expiring in " + date);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //Notifications are run by calling a notification manager and calling the notify function
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(ID, builder.build());
    }
}
