package com.example.khataregister.DB;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.khataregister.Model.Product;
import com.example.khataregister.UI.Activities.MainActivity;
import com.example.khataregister.Model.User;
import com.example.khataregister.Model.customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public class fireBaseDb implements fireBaseInterface {

    FirebaseDatabase database;
    DatabaseReference myRef;
    Context ctx;
    ProgressDialog progressDialog;
    public User userObj = new User();
    public ArrayList<Product>productList = new ArrayList<Product>();
    public ArrayList<customer>customersList = new ArrayList<customer>();

    public User getUserObj() {
        return userObj;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }



    public ArrayList<customer> getCustomersList() {
        return customersList;
    }

    public fireBaseDb(Context ctx) {
        this.ctx = ctx;
        initDb();
    }

    public void initDb()
    {
        if(myRef==null)
        {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            myRef = database.getReference();
        }
    }


    public ArrayList<Hashtable<String,String>>  dataCreation(DataSnapshot dataSnapshot)
    {
        ArrayList<Hashtable<String,String>> data = new ArrayList<Hashtable<String,String>>();
        for (DataSnapshot d : dataSnapshot.getChildren()) {
            GenericTypeIndicator<HashMap<String, Object>> type = new GenericTypeIndicator<HashMap<String, Object>>() {
            };
            HashMap<String, Object> map = d.getValue((GenericTypeIndicator<HashMap<String, Object>>) type);

            Hashtable<String, String> obj = new Hashtable<String, String>();
            for (String key : map.keySet()) {
                obj.put(key, map.get(key).toString());
            }
            data.add(obj);
        }

        return data;
    }



    private void createDialog(String message)
    {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(message); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    public void loadProducts()
    {
        createDialog("Loading Data");
        myRef.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    ArrayList<Hashtable<String, String>> data = dataCreation(dataSnapshot);
                    for (Hashtable<String, String> item: data) {
                        productList.add(new Product(
                                Integer.parseInt(item.get("id")),
                                item.get("name"),
                                Float.parseFloat(item.get("price")),
                                Integer.parseInt(item.get("quantity")),
                                item.get("date")));
                    }
                    MainActivity.loadCustomers(ctx);
                } catch (Exception ex) {
                    Log.e("firebasedb", ex.getMessage());
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firebasedb", "Failed to read value.", error.toException());
            }
        });

    }

    public void loadUsers(String tableName, String userEmail, String password) {


        createDialog("Validating User");
        myRef.child(tableName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    ArrayList<Hashtable<String, String>> data = dataCreation(dataSnapshot);
                    for (Hashtable<String, String> item: data) {
                        if (item.get("email").equals( userEmail) && item.get("password").equals( password) ){
                            userObj.setUserName(item.get("name"));
                            userObj.setEmail(item.get("email"));
                            userObj.setPassword(item.get("password"));
                            userObj.setTotalSales(Integer.parseInt(item.get("sales")));
                        }
                    }
                    progressDialog.dismiss();
                    if(userObj.getUserName()==null)
                    {
                        MainActivity.userNotFound(ctx);
                    }
                    MainActivity.userFound(ctx);
                } catch (Exception ex) {
                    Log.e("firebasedb", ex.getMessage());
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firebasedb", "Failed to read value.", error.toException());
            }
        });

    }

    public void loadCustomers() {
        myRef.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    ArrayList<Hashtable<String, String>> data = dataCreation(dataSnapshot);
                    for (Hashtable<String, String> item: data) {

                        customersList.add(new customer(
                                Integer.parseInt(item.get("id")),
                                item.get("name"),
                                item.get("pic"),
                                Float.parseFloat(item.get("receivable")
                                )
                        ));
                    }
                    MainActivity.setCustomersToUsers(ctx);
                } catch (Exception ex) {
                    Log.e("firebasedb", ex.getMessage());
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firebasedb", "Failed to read value.", error.toException());
            }
        });


    }
    private int getCustomerObject(int id, ArrayList<customer>customers)
    {
        for (int i=0; i<customers.size();i++)
        {
            if(customers.get(i).getId()==id)
            {
                return i;
            }
        }
        return -1;
    }

    private Product getProductObject(int id)
    {
        for ( Product obj : productList)
        {
            if(obj.getId()==id)
            {
                return obj;
            }
        }
        return null;
    }

    public void updateCustomers() {
        String userEmail = userObj.getEmail();
        ArrayList<customer>customers = userObj.getCustomers();
        myRef.child("User Customers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    ArrayList<Hashtable<String, String>> data = dataCreation(dataSnapshot);
                    for (Hashtable<String, String> item: data) {
                        String fireBaseEmail = item.get("email");
                        if(fireBaseEmail.equals(userEmail)) {
                            Set<String> setOfKeys = item.keySet();

                            for (String key : setOfKeys) {
                                if(!key.equals("email"))
                                {
                                customers.add(
                                        customersList.get(getCustomerObject(Integer.parseInt(item.get(key)),customersList
                                        )));
                            }
                            }
                        }

                    }
                    userObj.setCustomers(customers);
                    MainActivity.setProductsToCustomers(ctx);
                } catch (Exception ex) {
                    Log.e("firebasedb", ex.getMessage());
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firebasedb", "Failed to read value.", error.toException());
            }
        });

    }

    public void updateProducts() {

        ArrayList<customer>customers = userObj.getCustomers();
        myRef.child("Customer Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    int count,counter;
                    ArrayList<Hashtable<String, String>> data = dataCreation(dataSnapshot);
                    for (Hashtable<String, String> item: data) {
                        int custId = getCustomerObject(Integer.parseInt(item.get("custId")),customers);
                        if(custId!=-1) {
                            Set<String> setOfKeys = item.keySet();
                            ArrayList<Product> products = customers.get(custId).getProducts();
                            for (String key : setOfKeys) {
                                if (!key.equals("custId")) {

                                    products.add(
                                            getProductObject(
                                                    Integer.parseInt(item.get(key)
                                                    )));

                                }
                            }
                            customers.get(custId).setProducts(products);

                        }

                    }
                    progressDialog.dismiss();
                    userObj.setCustomers(customers);
                    MainActivity.loadUserProfile(userObj, ctx);
                } catch (Exception ex) {
                    Log.e("firebasedb", ex.getMessage());
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firebasedb", "Failed to read value.", error.toException());
            }
        });

    }

    public void saveAppData(User userObj) {

        createDialog("Saving Application Data");
        myRef.child("User").child(userObj.getEmail()).child("email").setValue(userObj.getEmail());
        myRef.child("User").child(userObj.getEmail()).child("name").setValue(userObj.getUserName());
        myRef.child("User").child(userObj.getEmail()).child("password").setValue(userObj.getPassword());
        myRef.child("User").child(userObj.getEmail()).child("sales").setValue(userObj.getTotalSales());

        int custId = 1;
        dataLayer sqlDb = new dataLayer(ctx);
        if(User.db==null)
            productList = Product.loadProductsFromSql(sqlDb.loadALL("productTable"));

        for(customer item : userObj.getCustomers())
        {
            myRef.child("Customer").child(Integer.toString(item.getId())).child("id").setValue(item.getId());
            myRef.child("Customer").child(Integer.toString(item.getId())).child("name").setValue(item.getName());
            myRef.child("Customer").child(Integer.toString(item.getId())).child("pic").setValue(item.getPic());
            myRef.child("Customer").child(Integer.toString(item.getId())).child("receivable").setValue(item.getReceivable());

            myRef.child("User Customers").child(userObj.getEmail()).child("email").setValue(userObj.getEmail());
            myRef.child("User Customers").child(userObj.getEmail()).child("custId_"+ custId).setValue(item.getId());
            myRef.child("Customer Products").child(Integer.toString(item.getId())).child("custId").setValue(item.getId());
            custId+=1;
            int Id = 1;

            for (Product obj : item.getProducts())
            {
                myRef.child("Customer Products").child(Integer.toString(item.getId())).child(obj.getName()).setValue(obj.getId());
                Id+=1;

                    Product prodID = getProductObject(obj.getId());
                    if (prodID == null) {
                        productList.add(obj);
                        myRef.child("Product").child(Integer.toString(obj.getId())).setValue(obj);
                    }



            }

        }

        progressDialog.dismiss();

    }
}


