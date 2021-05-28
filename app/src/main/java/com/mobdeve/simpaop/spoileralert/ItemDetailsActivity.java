package com.mobdeve.simpaop.spoileralert;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ItemDetailsActivity extends AppCompatActivity {

    //views
    private TextView nameTv, categoryTv, expiryTv, quantityTv, daysLeftTv2, daysTv2;
    private ImageView itemImageIv, proofIv;
    private Button editBtn, deleteBtn;
    private View viewBlock2;
    private int rowID;

    //database
    DatabaseHelper databaseHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        //database
        databaseHelper = new DatabaseHelper(this);

        //initialize views
        this.nameTv = findViewById(R.id.detailsNameTv);
        this.categoryTv = findViewById(R.id.detailsCategoryTv);
        this.expiryTv = findViewById(R.id.detailsExpiryTv);
        this.quantityTv = findViewById(R.id.detailsQuantityTv);
        this.itemImageIv = findViewById(R.id.detailsImageIv);
        this.proofIv = findViewById(R.id.detailsProofIv);
        this.editBtn = findViewById(R.id.detailsDeleteBtn);
        this.daysLeftTv2 = findViewById(R.id.daysLeftTv2);
        this.daysTv2 = findViewById(R.id.daysTv2);
        this.viewBlock2 = findViewById(R.id.daysColorView2);

        this.editBtn = findViewById(R.id.detailsEditBtn);
        this.deleteBtn = findViewById(R.id.detailsDeleteBtn);

        Intent i = getIntent();
        rowID = i.getIntExtra("ROWID", 0);

        //populate views by query using id from intent
        populateViews();

        //setlisteners for buttons edit and delete
        setListeners();
    }

    public void setListeners(){
        //create edit button onclick
        this.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItemDetailsActivity.this, InputItemActivity.class);
                i.putExtra("ROWID", rowID);
                i.putExtra(InputItemActivity.ACTIVITY_FROM, 1);
                startActivity(i);
                finish();

            }
        });

        //create delete button onclick
        this.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deleted = databaseHelper.deleteItem(String.valueOf(rowID));
                if(deleted > 0){
                    Intent i = new Intent(ItemDetailsActivity.this, MainActivity.class);
                    Intent intent = new Intent(ItemDetailsActivity.this, NotificationReceiver.class);
                    PendingIntent threeDaysBefore = PendingIntent.getBroadcast(getApplicationContext(), rowID, intent, 0);
                    PendingIntent onTheDay = PendingIntent.getBroadcast(getApplicationContext(), rowID * -1 , intent, 0);
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.cancel(threeDaysBefore);
                    am.cancel(onTheDay);
                    finish();
                    startActivity(i);
                }
            }
        });
    }

    //popoulates views with data from database
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void populateViews(){
        Cursor cursor = databaseHelper.getSpecificItem(rowID);
        if(cursor.moveToFirst()){
            String expiry = cursor.getString(4);
            this.nameTv.setText(cursor.getString(1));
            this.categoryTv.setText(cursor.getString(2));
            this.expiryTv.setText(expiry);
            this.quantityTv.setText(String.valueOf(cursor.getInt(3)));
            //Convert byte array into bitmap
            byte [] byteArray1 = cursor.getBlob(6);
            Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray1, 0 ,byteArray1.length);
            this.itemImageIv.setImageBitmap(bmp1);
            byte [] byteArray2 = cursor.getBlob(5);
            Bitmap bmp2 = BitmapFactory.decodeByteArray(byteArray2, 0 ,byteArray2.length);
            this.proofIv.setImageBitmap(bmp2);

            String currentDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

            SimpleDateFormat stf =  new SimpleDateFormat("MM/dd/yyyy");

            Date date1 = null;
            try {
                date1 = stf.parse(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date2 = null;
            try {
                date2 = stf.parse(expiry);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = date2.getTime() - date1.getTime();
            long days = (diff / (1000*60*60*24));

            if(days <= 1){
                if(days == 1){
                    daysLeftTv2.setText(String.valueOf(days));
                    daysTv2.setText("day");
                }
                else{
                    daysTv2.setText("Expired");
                    daysLeftTv2.setText(String.valueOf(0));
                }
                daysTv2.setTextColor(this.getColor(R.color.stop));
                viewBlock2.setBackgroundResource(R.drawable.stop);

            }else if(days > 1 && days <= 3){
                daysTv2.setText("days");
                daysTv2.setTextColor(this.getColor(R.color.ready));
                viewBlock2.setBackgroundResource(R.drawable.ready);
                daysLeftTv2.setText(String.valueOf(days));
            }else{
                daysTv2.setText("days");
                daysTv2.setTextColor(this.getColor(R.color.go));
                viewBlock2.setBackgroundResource(R.drawable.go);
                daysLeftTv2.setText(String.valueOf(days));
            }
        }
    }
}