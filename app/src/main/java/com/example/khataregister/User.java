package com.example.khataregister;

import android.content.Context;

import java.util.ArrayList;
import java.util.Hashtable;

public class User {

    private Float receivables;
    public ArrayList<customer>customers = new ArrayList<customer>();
    public static fireBaseDb db = null;

    private String userName, password, email;
    private int totalSales;


    public User( String userName, String password, String email, int totalSales, Float recev,  ArrayList<customer> customers) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.receivables = recev;
        this.totalSales = totalSales;
        this.customers = customers;
    }

    public static User generateUser(ArrayList<Hashtable<String,String>>users)
    {
        if(users==null) return null;
        Float rec = Float.valueOf(0);
        for( Hashtable<String,String> item : users)
        {
            int val = Integer.parseInt(item.get("sales"));
            return new User(
                    item.get("name"),
                    item.get("password"),
                    item.get("email"),
                    val,rec,
                    new ArrayList<customer>());
        }
        return null;
    }
    public static void loadUser(Context context) {

        dataLayer sqlDb = new dataLayer(context);
        MainActivity.userObj = generateUser(sqlDb.loadALL("userTable"));
        if(MainActivity.userObj==null)  return;

        MainActivity.userObj.setCustomers(customer.generateCustomers(sqlDb.loadALL("customerTable")));
        ArrayList<Product> products = Product.loadProductsFromSql(sqlDb.loadALL("productTable"));
        Product.updateCustomerProducts(products, sqlDb.loadALL("ProductsBought"));
    }

    public static void saveDataToFireBase(Context context) {
        if(db==null)
        {
            db = new fireBaseDb(context);
        }

        db.saveAppData(MainActivity.userObj);


    }

    public Float getReceivables() {
        return receivables;
    }

    public void setReceivables(Float receivables) {
        this.receivables = receivables;
    }


    public User(){};

    public static void getUser(Context context, String email, String password)
    {
        if(db==null)
        {
            db = new fireBaseDb(context);
        }

       db.loadUsers("User", email, password);
    }


    public static void updateCustomers(Context ctx) {

        if(db==null)
        {
            db = new fireBaseDb(ctx);
        }

        db.updateCustomers();
    }

    public static void truncateDb(Context ctx)
    {
        dataLayer sqlDb = new dataLayer(ctx);
        sqlDb.truncateDb();
    }

    public static void saveDataToSql(Context ctx) {
        dataLayer sqlDb = new dataLayer(ctx);
        sqlDb.truncateDb();

        User obj= MainActivity.userObj;

        if(db!=null) {
            obj = db.getUserObj();
        }

        Hashtable<String,String>user = new Hashtable<String,String>();
        user.put("name", obj.getUserName());
        user.put("email",obj.getEmail());
        user.put("password",obj.getPassword());
        user.put("sales", Integer.toString(obj.getTotalSales()));
        sqlDb.save(user, "userTable");

        obj.receivables = Float.valueOf(0);

        if(obj.getCustomers()!=null) {
            for (customer item : obj.getCustomers()) {
                obj.receivables += item.getReceivable();
                customer.addCustomer(item, ctx);
            }
        }
        else
        {
            obj.setCustomers(new ArrayList<customer>());
        }

        if(db!=null) {
            for (Product nProduct : db.getProductList()) {
                Product.AddProductOnly(nProduct, sqlDb);
            }
        }
    }

    public static User getUser() {
       return User.db.getUserObj();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public ArrayList<customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<customer> customers) {
        this.customers = customers;
    }


}
