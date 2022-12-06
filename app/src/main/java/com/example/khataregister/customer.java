package com.example.khataregister;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Hashtable;

public class customer implements Parcelable {
    int id;
    private String name;
    private String pic;
    public ArrayList<Product> products= new ArrayList<Product>();
    private float receivable;

    public customer(int id, String name, String pic, ArrayList<Product> products, float receivable) {
        this.name = name;
        this.pic = pic;
        this.id = id;
        this.products = products;
        this.receivable = receivable;
    }

    public customer(int id, String name, String pic, float receivable) {
        this.name = name;
        this.pic = pic;
        this.id = id;
        this.products = products;
        this.receivable = receivable;
    }

    protected customer(Parcel in) {
        id = in.readInt();
        name = in.readString();
        pic = in.readString();
        products = in.createTypedArrayList(Product.CREATOR);
        receivable = in.readFloat();
    }

    public static final Creator<customer> CREATOR = new Creator<customer>() {
        @Override
        public customer createFromParcel(Parcel in) {
            return new customer(in);
        }

        @Override
        public customer[] newArray(int size) {
            return new customer[size];
        }
    };



    public static void updateBalance(String name, String id, String price, Context context) {

        dataLayer db = new dataLayer(context);
        Hashtable<String,String>customer= new Hashtable<String,String>();
        customer.put("id", id);
        customer.put("name",name);
        customer.put("img","user");
        customer.put("receivable", price);

        int custIndex = getCustomerObject(Integer.parseInt(id) , MainActivity.userObj.getCustomers());
        MainActivity.userObj.getCustomers().get(custIndex).setReceivable(Float.parseFloat(price));

        db.updateBalance(customer);
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

    public static void loadCustomers(Context ctx) {
        if(User.db==null)
        {
            User.db = new fireBaseDb(ctx);
        }
        User.db.loadCustomers();
    }

    public static void updateProducts(Context ctx) {
        if(User.db==null)
        {
            User.db = new fireBaseDb(ctx);
        }
        User.db.updateProducts();

    }

    public static ArrayList<customer> generateCustomers(ArrayList<Hashtable<String, String>> customersList) {
        ArrayList<customer>customerList = new ArrayList<>();

        for (Hashtable<String,String>item : customersList)
        {
            customerList.add(
                    new customer(
                            Integer.parseInt(item.get("id")),
                            item.get("name"),
                            item.get("img"),
                            new ArrayList<Product>(),
                            Float.parseFloat(item.get("receivable")))
            );
        }
        return customerList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public float getReceivable() {
        return receivable;
    }

    public void setReceivable(float receivable) {
        this.receivable = receivable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(pic);
        parcel.writeTypedList(products);
        parcel.writeFloat(receivable);
    }

    public static ArrayList<Hashtable<String,String>> getProductList(ArrayList<Product>productList, int id)
    {
        ArrayList<Hashtable<String,String>>products= new ArrayList<Hashtable<String,String>>();

        for( Product item : productList)
        {
            Hashtable<String,String> obj=new Hashtable<String,String>();
            obj.put("prodid",Integer.toString(item.getId()));
            obj.put("custId", Integer.toString(id));
            products.add(obj);
        }
        return products;
    }

    public static void addCustomer(customer cust, Context context) {
        dataLayer dbHelper = new dataLayer(context);
        ArrayList<Hashtable<String,String>>products = new ArrayList<Hashtable<String,String>>();
        if(cust.getProducts()!=null)
            products = cust.getProductList(cust.products, cust.id);

        if(cust.getPic()==null)
        {
            cust.setPic("user");
            cust.setReceivable(-1);
        }
        Hashtable<String,String>customer= new Hashtable<String,String>();
        customer.put("name",cust.getName());
        customer.put("img",cust.getPic());
        if(cust.getReceivable()==-1)
            customer.put("receivable","no receivable");
        else
        {
            customer.put("receivable",Float.toString(cust.getReceivable()));
        }

        if(products.size()>0)
        {
            dbHelper.save(products, "ProductsBought");
        }

        dbHelper.save(customer, "customerTable");
        if(customer.get("receivable").equals("no receivable"))
            cust.setId(dbHelper.getCustomerId());
    }
}

