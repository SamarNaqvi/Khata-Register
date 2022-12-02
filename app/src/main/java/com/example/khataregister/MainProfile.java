package com.example.khataregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;


public class MainProfile extends AppCompatActivity {
    static ArrayList<customer> exampleList = new ArrayList<>();
    RecyclerView rcv;
    customerList a ;
    private final int GALLERY_REQ_CODE=1000;
    EditText name;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_profile);

        name = findViewById(R.id.name);
        rcv=(RecyclerView) findViewById(R.id.recyclerView);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        a =  new customerList(this);
        a.setArrays(exampleList);
        rcv.setAdapter(a);

    }

    public void onClick(View v){
        customer c = new customer(name.getText().toString());
        exampleList.add(c);
        a =  new customerList(this);
        a.setArrays(exampleList);
        rcv.setAdapter(a);
    }



}