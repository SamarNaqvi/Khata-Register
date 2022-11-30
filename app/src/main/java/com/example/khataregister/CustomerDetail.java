package com.example.khataregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerDetail extends AppCompatActivity {

    CustomerAdaptor adaptor;
    ArrayList<Product>data = new ArrayList<Product>();
    Customer cust;
    ImageView img;
    TextView name, payable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        createData();

        RecyclerView recyclerView = findViewById(R.id.products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new CustomerAdaptor(data);

        recyclerView.setAdapter(adaptor);




    }

    private void createData() {
        Intent intent = getIntent();
        data = intent.getExtras().getParcelableArrayList("products");

        name = findViewById(R.id.name);
        payable = findViewById(R.id.payable);
        img  = findViewById(R.id.picture);

        String imgRsc = intent.getStringExtra("img");
        int rscId = getResources().getIdentifier(imgRsc, "drawable", getPackageName());
        img.setImageResource(rscId);

        name.setText(intent.getStringExtra("name"));
        payable.setText(intent.getStringExtra("price"));

    }

}