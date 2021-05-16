package com.mobdeve.simpaop.spoileralert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private DatePickerDialog.OnDateSetListener expiryDateListener;
    private ItemAdapter adapter;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_item);

        databaseHelper = new DatabaseHelper(this);

        this.itemNameEt = findViewById(R.id.itemNameEt);
        this.itemCategoryEt = findViewById(R.id.itemCategoryEt);
        this.quantityEt = findViewById(R.id.quantityEt);


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
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        expiryDateInput.setText(sdf.format(calendar.getTime()));
    }

    public void addItem(View view){
        boolean isInserted = databaseHelper.insertItem(itemNameEt.getText().toString(), itemCategoryEt.getText().toString(), expiryDateInput.getText().toString(), Integer.parseInt(quantityEt.getText().toString()));
        if(isInserted = true)
            Toast.makeText(view.getContext(), "data inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(view.getContext(), "not inserted", Toast.LENGTH_LONG).show();
        //adapter.addItem();
        finish();
    }
}