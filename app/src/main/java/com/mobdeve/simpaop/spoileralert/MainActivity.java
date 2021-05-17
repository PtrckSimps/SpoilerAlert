package com.mobdeve.simpaop.spoileralert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button addItemBtn;
    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private RecyclerView recyclerView;

    private ItemAdapter adapter;
    private RecyclerView.LayoutManager manager;

    //database
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        this.addItemBtn = findViewById(R.id.addItemBtn);

        this.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputItemActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        itemArrayList.clear();
        getItems();
        adapter = new ItemAdapter(itemArrayList);
        recyclerView.setAdapter(adapter);
    }

    public void getItems(){
        Cursor items = databaseHelper.getItems();
        if(items.getCount() == 0){
            Log.d(TAG, "error");
            return;
        }else{
            while(items.moveToNext()){
                int rowid = items.getInt(0);
                String itemname = items.getString(1);
                String category = items.getString(2);
                int quantity = items.getInt(3);
                String expiry = items.getString(4);
                byte[] proof = items.getBlob(5);
                byte[] itemimage = items.getBlob(6);
                Log.d(TAG, itemname);
                this.itemArrayList.add(new Item(itemname, category, quantity, expiry, getImage(proof), getImage(itemimage)));
            }
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}