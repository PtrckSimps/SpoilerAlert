package com.mobdeve.simpaop.spoileralert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailsActivity extends AppCompatActivity {
    private TextView nameTv, categoryTv, expiryTv, quantityTv;
    private ImageView itemImageIv, proofIv, trashIv;
    private Button editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        this.nameTv = findViewById(R.id.detailsNameTv);
        this.categoryTv = findViewById(R.id.detailsCategoryTv);
        this.expiryTv = findViewById(R.id.detailsExpiryTv);
        this.quantityTv = findViewById(R.id.detailsQuantityTv);
        this.itemImageIv = findViewById(R.id.detailsImageIv);
        this.proofIv = findViewById(R.id.detailsProofIv);
        this.trashIv = findViewById(R.id.detailsTrashIv);
        this.editBtn = findViewById(R.id.detailsEditBtn);

        Intent i = getIntent();
        this.nameTv.setText(i.getStringExtra("NAME_KEY"));
        this.categoryTv.setText(i.getStringExtra("CATEGORY_KEY"));
        this.expiryTv.setText("Expiring: " + i.getStringExtra("EXPIRY_KEY"));
        this.quantityTv.setText("Qty: " + String.valueOf(i.getIntExtra("QUANTITY_KEY", 1)));
        //Convert byte array into bitmap
        byte [] byteArray1 = getIntent().getByteArrayExtra("IMAGE_KEY");
        Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray1, 0 ,byteArray1.length);
        this.itemImageIv.setImageBitmap(bmp1);
        byte [] byteArray2 = getIntent().getByteArrayExtra("PROOF_KEY");
        Bitmap bmp2 = BitmapFactory.decodeByteArray(byteArray2, 0 ,byteArray2.length);
        this.proofIv.setImageBitmap(bmp2);

    }
}