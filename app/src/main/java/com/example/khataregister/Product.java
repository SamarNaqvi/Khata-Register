package com.example.khataregister;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Hashtable;

public class Product implements Parcelable {
    private int id;
    private String name;
    private float price;
    private int quantity;
    private String date;

    public Product(int id, String name, float price, int quantity, String d) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = d;
        this.quantity = quantity;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readFloat();
        quantity = in.readInt();
        date = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public static void loadProducts(Context ctx)
    {
        if(User.db==null)
        {
            User.db = new fireBaseDb(ctx);
        }
        User.db.loadProducts();
    }

    public static void updateCustomerProducts(ArrayList<Product> products, ArrayList<Hashtable<String, String>> productsBought) {


        for ( Hashtable<String,String>item : productsBought)
        {
            int custIndex = Product.getCustomerObject(Integer.parseInt(item.get("custid")),MainActivity.userObj.getCustomers());
            int prodIndex = getProductObject(Integer.parseInt(item.get("prodid")), products);
            MainActivity.userObj.getCustomers().get(custIndex).getProducts().add(products.get(prodIndex));
        }

    }

    private static int getCustomerObject(int id, ArrayList<customer>customers)
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

    private static int getProductObject(int id, ArrayList<Product>products)
    {
        for (int i=0; i<products.size();i++)
        {
            if(products.get(i).getId()==id)
            {
                return i;
            }
        }
        return -1;
    }

    public static void addProduct(Product obj, Context context, int custID)
    {
        ArrayList<Product>list = new ArrayList<Product>();
        list.add(obj);
        dataLayer dbHelper = new dataLayer(context);
        ArrayList<Hashtable<String,String>>prods = customer.getProductList(list, custID);
        dbHelper.save(prods,"ProductsBought");

        ArrayList<customer>customers = MainActivity.userObj.getCustomers();
        custID = obj.getCustomerObject(custID, customers);
        ArrayList<Product>custProducts = customers.get(custID).getProducts();
        for ( Product prod : custProducts)
        {
            if(prod.getName() == obj.getName())
            {
                return;
            }
        }

        MainActivity.userObj.getCustomers().get(custID).getProducts().add(obj);
        AddProductOnly(obj, dbHelper);

    }

    public static void AddProductOnly(Product obj, dataLayer dbSql)
    {
        Hashtable<String,String>newProduct = new Hashtable<String, String>();
        newProduct.put("id",Integer.toString(obj.getId()));
        newProduct.put("name",obj.getName());
        newProduct.put("price",Float.toString(obj.getPrice()));
        newProduct.put("quantity",Integer.toString(obj.getQuantity()));
        newProduct.put("date",obj.getDate());

        dbSql.save(newProduct,"productTable");
    }
    public static ArrayList<Product> loadProductsFromSql(ArrayList<Hashtable<String, String>> products) {
        ArrayList<Product>productList = new ArrayList<>();

        for (Hashtable<String,String>item : products)
        {
            productList.add(
                    new Product(
                            Integer.parseInt(item.get("id")),
                            item.get("name"),
                            Float.parseFloat(item.get("price")),
                            Integer.parseInt(item.get("quantity")),
                            item.get("date")
                    )
            );
        }
        return productList;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return this.date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeFloat(price);
        parcel.writeInt(quantity);
        parcel.writeString(date);
    }
}
