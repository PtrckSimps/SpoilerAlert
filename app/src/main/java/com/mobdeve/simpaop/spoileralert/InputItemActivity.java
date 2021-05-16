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

import java.util.Calendar;
import java.util.Random;

public class InputItemActivity extends AppCompatActivity {

    private static final String TAG = "InputItemActivity";

    private TextView expiryDateInput;
    private EditText itemNameEt;
    private EditText itemCategoryEt;
    private DatePickerDialog.OnDateSetListener expiryDateListener;
    private ItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_item);

        this.expiryDateInput = findViewById(R.id.expiryDateInput);
        this.itemNameEt = findViewById(R.id.itemNameEt);
        this.itemCategoryEt = findViewById(R.id.itemCategoryEt);

        expiryDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
            }
        });

        //adapter = new ItemAdapter();
    }

    private void initDatePicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(InputItemActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, expiryDateListener, year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        expiryDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = month + "/" + dayOfMonth + "/" + year;
                expiryDateInput.setText(date);
            }
        };
    }
    /*
    public void addItem(View view){
        Random rand = new Random();
        //(4) Items can still be added on the RecyclerView by talking to the adapter
        adapter.addItem(itemNameEt.getText().toString(), itemCategoryEt.getText().toString(), expiryDateInput.getText().toString());
        finish();
    }
    */
}