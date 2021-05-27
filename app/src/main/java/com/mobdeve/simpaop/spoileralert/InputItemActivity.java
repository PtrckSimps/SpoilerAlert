package com.mobdeve.simpaop.spoileralert;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class InputItemActivity extends AppCompatActivity {


    public static final String ACTIVITY_FROM = "ACTIVITY_FROM";
    private static final String CHANNEL_ID = "SPOILERALERT";
    private static final String TAG = "InputItemActivity";

    //views
    private TextView expiryDateInput, addItemHeaderTv;
    private EditText itemNameEt;
    private EditText itemCategoryEt;
    private EditText quantityEt;
    private Button updateItembtn;
    private ImageView itemPictureIv, itemExpiryIv;
    private FloatingActionButton addPictureBtn, addExpiryBtn;

    private int activityFrom, rowID;
    private int REQUEST_CAMERA=1, SELECT_FILE=0, REQUEST_CAMERA2=10, SELECT_FILE2=20;
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();

    //database
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_item);

        //database
        databaseHelper = new DatabaseHelper(this);

        //initialize views
        this.itemNameEt = findViewById(R.id.itemNameEt);
        this.itemCategoryEt = findViewById(R.id.itemCategoryEt);
        this.quantityEt = findViewById(R.id.quantityEt);
        this.addPictureBtn = findViewById(R.id.addPictureBtn);
        this.itemPictureIv = findViewById(R.id.itemPictureIv);
        this.itemExpiryIv = findViewById(R.id.itemExpiryIv);
        this.addExpiryBtn = findViewById(R.id.addExpiryProofBtn);
        this.expiryDateInput = findViewById(R.id.expiryDateInput);
        this.updateItembtn = findViewById(R.id.updateItembtn);
        this.addItemHeaderTv = findViewById(R.id.addItemHeaderTv);

        setListeners();

        // 0 -> came from MainActivity; 1 -> came from CartActivity
        Intent i = getIntent();

        this.rowID = i.getIntExtra("ROWID", 0);
        this.activityFrom = i.getIntExtra(ACTIVITY_FROM, 0);

        if(activityFrom == 0) {
            Log.d(TAG, "setViewContent: 0");

        } else {
            Log.d(TAG, "setViewContent: 1");
            this.addItemHeaderTv.setText("Update Item");
            this.updateItembtn.setVisibility(View.VISIBLE);
            populateEditText();
        }

    }

    public void setListeners(){
        //Date input with datepicker dialog
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 00);
                calendar.set(Calendar.MINUTE, 00);
                calendar.set(Calendar.SECOND, 00);
                calendar2.set(Calendar.YEAR, year);
                calendar2.set(Calendar.MONTH, monthOfYear);
                calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar2.set(Calendar.HOUR_OF_DAY, 00);
                calendar2.set(Calendar.MINUTE, 00);
                calendar2.set(Calendar.SECOND, 00);
                calendar2.add(calendar2.DATE, -3);

                Log.d(TAG, "og "+ calendar.getTime());
                Log.d(TAG, "decremented "+ calendar2.getTime());

                /*
                // Test if the times are in the past, if they are add one day
                Calendar now = Calendar.getInstance();
                if(now.after(calendar))
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                if(now.after(calendar2))
                    calendar2.add(Calendar.HOUR_OF_DAY, 24);
                */
                updateLabel();
            }

        };

        //textview listener for date inpt
        expiryDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpg = new DatePickerDialog(InputItemActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpg.show();
            }
        });

        if(ContextCompat.checkSelfPermission(InputItemActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(InputItemActivity.this, new String[] {
                    Manifest.permission.CAMERA
            }, 100);
        }

        //add picture listener
        addPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(1);
                /*
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
                */

            }
        });

        //add picture listener
        addExpiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(2);
                /*
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
                */
            }
        });
    }

    //update date TextView with chosen date
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        expiryDateInput.setText(sdf.format(calendar.getTime()));
    }

    private void SelectImage(int from){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(InputItemActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(from == 1){
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }else{
                        startActivityForResult(intent, REQUEST_CAMERA2);
                    }

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    if(from == 1){
                        startActivityForResult(intent, SELECT_FILE);
                    }else{
                        startActivityForResult(intent, SELECT_FILE2);
                    }

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                itemPictureIv.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                itemPictureIv.setImageURI(selectedImageUri);

            }else if(requestCode==REQUEST_CAMERA2){
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                itemExpiryIv.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE2){
                Uri selectedImageUri = data.getData();
                itemExpiryIv.setImageURI(selectedImageUri);
            }

        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    //add item method, calls insertItem from database
    public void addItem(View view){

        if(itemNameEt.getText().toString().equals("") || itemCategoryEt.getText().toString().equals("") || quantityEt.getText().toString().equals("") || Integer.parseInt(quantityEt.getText().toString()) < 0 || expiryDateInput.getText().toString().equals("Select date")){
            showErrorMessage();
        }else{
            //getting bitmap image then converting to byte for sqlite storage
            itemPictureIv.invalidate();
            BitmapDrawable drawable1 = (BitmapDrawable)itemPictureIv.getDrawable();
            Bitmap bitmap1 = drawable1.getBitmap();

            itemExpiryIv.invalidate();
            BitmapDrawable drawable2 = (BitmapDrawable)itemExpiryIv.getDrawable();
            Bitmap bitmap2 = drawable2.getBitmap();

            byte[] image1 = getBytes(bitmap1);
            byte[] image2 = getBytes(bitmap2);

            long isInserted = databaseHelper.insertItem(itemNameEt.getText().toString(), itemCategoryEt.getText().toString(),  Integer.parseInt(quantityEt.getText().toString()), expiryDateInput.getText().toString(), image2, image1);
            Log.d(TAG, "ID: " + isInserted);

            if(isInserted != -1)
                Toast.makeText(view.getContext(), "Item added to the inventory", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(view.getContext(), "Item not added", Toast.LENGTH_LONG).show();

            setupNotification(isInserted, itemNameEt.getText().toString(), expiryDateInput.getText().toString());
            finish();
        }

    }

    public void populateEditText(){
        Cursor cursor = databaseHelper.getSpecificItem(rowID);
        if(cursor.moveToFirst()) {
            String expiry = cursor.getString(4);
            this.itemNameEt.setText(cursor.getString(1));
            this.itemCategoryEt.setText(cursor.getString(2));
            this.expiryDateInput.setText(expiry);
            this.quantityEt.setText(String.valueOf(cursor.getInt(3)));
            //Convert byte array into bitmap
            byte[] byteArray1 = cursor.getBlob(6);
            Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
            this.itemPictureIv.setImageBitmap(bmp1);
            byte[] byteArray2 = cursor.getBlob(5);
            Bitmap bmp2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray1.length);
            this.itemExpiryIv.setImageBitmap(bmp2);
        }
    }

    public void updateItem(View view){
        if(itemNameEt.getText().toString().equals("") || itemCategoryEt.getText().toString().equals("") || quantityEt.getText().toString().equals("") || Integer.parseInt(quantityEt.getText().toString()) < 0 || expiryDateInput.getText().toString().equals("Select date")){
            showErrorMessage();
        }else{
            //getting bitmap image then converting to byte for sqlite storage
            itemPictureIv.invalidate();
            BitmapDrawable drawable1 = (BitmapDrawable)itemPictureIv.getDrawable();
            Bitmap bitmap1 = drawable1.getBitmap();

            itemExpiryIv.invalidate();
            BitmapDrawable drawable2 = (BitmapDrawable)itemExpiryIv.getDrawable();
            Bitmap bitmap2 = drawable2.getBitmap();

            byte[] image1 = getBytes(bitmap1);
            byte[] image2 = getBytes(bitmap2);

            boolean isUpdated = databaseHelper.updateItem(String.valueOf(rowID), itemNameEt.getText().toString(), itemCategoryEt.getText().toString(),  Integer.parseInt(quantityEt.getText().toString()), expiryDateInput.getText().toString(), image2, image1);
            if(isUpdated = true)
                Toast.makeText(view.getContext(), "Item details updated", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(view.getContext(), "Item details not updated", Toast.LENGTH_LONG).show();

            updateNotification (rowID, itemNameEt.getText().toString(), expiryDateInput.getText().toString());

            finish();
        }
    }

    private void showErrorMessage() {
        Toast t = Toast.makeText(
                InputItemActivity.this,
                "Please ensure that all input information are correct.",
                Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("EXPIRY", expiryDateInput.getText().toString());
        BitmapDrawable drawable1 = (BitmapDrawable)itemPictureIv.getDrawable();
        Bitmap bitmap1 = drawable1.getBitmap();
        outState.putByteArray("ITEM_IMAGE",getBytes(bitmap1));

        BitmapDrawable drawable2 = (BitmapDrawable)itemExpiryIv.getDrawable();
        Bitmap bitmap2 = drawable2.getBitmap();
        outState.putByteArray("EXPIRY_IMAGE",getBytes(bitmap2));

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        expiryDateInput.setText(savedInstanceState.getString("EXPIRY"));

        byte[] byteArray1 = savedInstanceState.getByteArray("ITEM_IMAGE");
        Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
        this.itemPictureIv.setImageBitmap(bmp1);

        byte[] byteArray2 = savedInstanceState.getByteArray("EXPIRY_IMAGE");
        Bitmap bmp2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
        this.itemExpiryIv.setImageBitmap(bmp2);

    }

    private void setupNotification(long ID, String itemName, String expiryDate){

        createNotificationChannel();

        Calendar current = Calendar.getInstance();
        int diff = calendar2.compareTo(current);

        // Create two different PendingIntents, they MUST have different requestCodes
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("ID", (int) ID);
        intent.putExtra("NAME", itemName);
        intent.putExtra("DATE", expiryDate);

        if(diff != -1){

            Log.d(TAG, "setupNotification: two notifs");
            PendingIntent threeDaysBefore = PendingIntent.getBroadcast(getApplicationContext(), (int) ID, intent, 0);
            PendingIntent onTheDay = PendingIntent.getBroadcast(getApplicationContext(), (int) ID * -1 , intent, 0);

            // Start both alarms, set to repeat once every day
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), threeDaysBefore);
            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), threeDaysBefore);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), threeDaysBefore);
            }
        }else{
            Log.d(TAG, "setupNotification: one notif");
            PendingIntent onTheDay = PendingIntent.getBroadcast(getApplicationContext(), (int) ID * -1 , intent, 0);

            // Start both alarms, set to repeat once every day
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);

            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);

            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);

            }
        }

    }

    private void updateNotification(int ID, String itemName, String expiryDate){
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent threeDaysBefore = PendingIntent.getBroadcast(getApplicationContext(), rowID, intent, 0);
        PendingIntent onTheDay = PendingIntent.getBroadcast(getApplicationContext(), rowID * -1 , intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(threeDaysBefore);
        am.cancel(onTheDay);
        createNotificationChannel();
        intent.putExtra("ID", ID);
        intent.putExtra("NAME", itemName);
        intent.putExtra("DATE", expiryDate);

        Calendar current = Calendar.getInstance();
        int diff = calendar2.compareTo(current);

        if(diff != -1){
            threeDaysBefore = PendingIntent.getBroadcast(getApplicationContext(),  ID, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
            onTheDay = PendingIntent.getBroadcast(getApplicationContext(),  ID * -1 , intent,  PendingIntent.FLAG_UPDATE_CURRENT);

            // Start both alarms, set to repeat once every day
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), threeDaysBefore);
            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), threeDaysBefore);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), threeDaysBefore);
            }
        }else{
            onTheDay = PendingIntent.getBroadcast(getApplicationContext(),  ID * -1 , intent,  PendingIntent.FLAG_UPDATE_CURRENT);

            // Start both alarms, set to repeat once every day
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), onTheDay);
            }
        }

    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Spoiler Alert", importance);
            channel.setDescription("Fram App channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Log.d(TAG, "createNotificationChannel: ");
        }
    }
}