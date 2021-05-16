package com.mobdeve.simpaop.spoileralert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button addItemBtn;
    private ArrayList<Item> itemArrayList;
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
        adapter = new ItemAdapter(itemArrayList);
        recyclerView.setAdapter(adapter);
    }

}