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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.addItemBtn = findViewById(R.id.addItemBtn);

        this.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputItemActivity.class);
                startActivity(intent);
            }
        });

        generateData();

        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new ItemAdapter(itemArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void generateData(){
        this.itemArrayList = new ArrayList<>();

        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2021, 6, 08",
                14, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2021, 12,13",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2021, 12,06",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2020, 7,15",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Watermelon", "Fruits",
                "2021, 6,31",
                5, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Carrots", "Vegetables",
                "2020, 12,31",
                21, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Eggplant", "Vegetables",
                "2021, ,31",
                10, R.drawable.ic_launcher_background));
        this.itemArrayList.add(new Item(
                "Potato", "Vegetables",
                "2021, 5,16",
                14, R.drawable.ic_launcher_background));
        this.itemArrayList .add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList.add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList.add(new Item(
                "Seedless Grapes", "Fruits",
                "2021, 5,4",
                2, R.drawable.ic_launcher_background));
        this.itemArrayList.add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList.add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
        this.itemArrayList.add(new Item(
                "Tomato", "Vegetables",
                "2020, 12,31",
                23, R.drawable.ic_launcher_background));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }
}