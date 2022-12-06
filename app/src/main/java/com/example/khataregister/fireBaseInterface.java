package com.example.khataregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public interface fireBaseInterface {

    public void loadProducts();
    public void loadUsers(String tableName, String userEmail, String password);
    public void loadCustomers();
    public void updateCustomers();
    public void updateProducts() ;

}
