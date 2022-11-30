package com.example.khataregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Product> data=  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createData();

        Button showDetails = findViewById(R.id.showDetails);

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, CustomerDetail.class);

                intent.putExtra("name","Samar");
                intent.putExtra("price","200");
                intent.putExtra("img","user");

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("products", data);
                intent.putExtras(bundle);


                startActivity(intent);
            }
        });

    }

    public void createData()
    {

        data.add(new Product(1,"p1",12,3,"123"));
        data.add(new Product(2,"p1",12,3,"123"));
        data.add(new Product(3,"p1",12,3,"123"));
        data.add(new Product(4,"p1",12,3,"123"));
    }

}