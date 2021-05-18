package com.mobdeve.simpaop.spoileralert;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class InputItemActivity extends AppCompatActivity {

    //
    public static final String ACTIVITY_FROM = "ACTIVITY_FROM";

    private static final String TAG = "InputItemActivity";

    //views
    private TextView expiryDateInput, addItemHeaderTv;
    private EditText itemNameEt;
    private EditText itemCategoryEt;
    private EditText quantityEt;
    private Calendar calendar = Calendar.getInstance();
    private Button addPictureBtn, addExpiryBtn, updateItembtn;
    private ImageView itemPictureIv, itemExpiryIv;

    private int activityFrom, rowID;
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

        //Date input with datepicker dialog
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        //textview listener for date inpt
        expiryDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(InputItemActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        //add picture listener
        addExpiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 200);
            }
        });

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

    //update date TextView with chosen date
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        expiryDateInput.setText(sdf.format(calendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            itemPictureIv.setImageBitmap(Bitmap.createScaledBitmap(captureImage, 240, 240, false));
        }
        else if (requestCode == 200) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            itemExpiryIv.setImageBitmap(Bitmap.createScaledBitmap(captureImage, 240, 240, false));
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
        //getting bitmap image then converting to byte for sqlite storage
        itemPictureIv.invalidate();
        BitmapDrawable drawable1 = (BitmapDrawable)itemPictureIv.getDrawable();
        Bitmap bitmap1 = drawable1.getBitmap();

        itemExpiryIv.invalidate();
        BitmapDrawable drawable2 = (BitmapDrawable)itemExpiryIv.getDrawable();
        Bitmap bitmap2 = drawable2.getBitmap();

        byte[] image1 = getBytes(bitmap1);
        byte[] image2 = getBytes(bitmap2);

        boolean isInserted = databaseHelper.insertItem(itemNameEt.getText().toString(), itemCategoryEt.getText().toString(),  Integer.parseInt(quantityEt.getText().toString()), expiryDateInput.getText().toString(), image2, image1);
        if(isInserted = true)
            Toast.makeText(view.getContext(), "Item added to the inventory", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(view.getContext(), "Item not added", Toast.LENGTH_LONG).show();
        finish();
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
            Bitmap bmp2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
            this.itemExpiryIv.setImageBitmap(bmp2);
        }
    }

    public void updateItem(View view){
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
        finish();
    }
}