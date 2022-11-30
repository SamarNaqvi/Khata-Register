package com.example.khataregister;

import android.os.Parcel;
import android.os.Parcelable;

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
