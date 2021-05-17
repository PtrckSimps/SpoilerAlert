package com.mobdeve.simpaop.spoileralert;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

    private static final String TAG = "InputItemActivity";

    private TextView expiryDateInput;
    private EditText itemNameEt;
    private EditText itemCategoryEt;
    private EditText quantityEt;
    private Calendar calendar = Calendar.getInstance();
    private Button addPictureBtn, addExpiryBtn;
    private ImageView itemPictureIv, itemExpiryIv;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_item);

        databaseHelper = new DatabaseHelper(this);

        this.itemNameEt = findViewById(R.id.itemNameEt);
        this.itemCategoryEt = findViewById(R.id.itemCategoryEt);
        this.quantityEt = findViewById(R.id.quantityEt);
        this.addPictureBtn = findViewById(R.id.addPictureBtn);
        this.itemPictureIv = findViewById(R.id.itemPictureIv);
        this.itemExpiryIv = findViewById(R.id.itemExpiryIv);
        this.addExpiryBtn = findViewById(R.id.addExpiryProofBtn);


        this.expiryDateInput = findViewById(R.id.expiryDateInput);


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
        addPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        addExpiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 200);
            }
        });

    }

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

    public void addItem(View view){
        //getting bitmpa image then converting to byte for sqlite storage
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
}