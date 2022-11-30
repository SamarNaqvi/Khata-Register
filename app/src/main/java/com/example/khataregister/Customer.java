package com.example.khataregister;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Customer implements Parcelable {
    private String name;
    private String pic;
    private ArrayList<Product>products;
    private float receivable;

    public Customer(String name, String pic, ArrayList<Product> products, float receivable) {
        this.name = name;
        this.pic = pic;
        this.products = products;
        this.receivable = receivable;
    }

    protected Customer(Parcel in) {
        name = in.readString();
        pic = in.readString();
        receivable = in.readFloat();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

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
        parcel.writeString(name);
        parcel.writeString(pic);
        parcel.writeFloat(receivable);
    }
}

