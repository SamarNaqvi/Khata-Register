package com.example.khataregister;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdaptor extends RecyclerView.Adapter<CustomerAdaptor.CustomerViewHolder>{

    private ArrayList<Product> products= new ArrayList<Product>();

    public CustomerAdaptor(ArrayList<Product> Items) {
        this.products = Items;
    }


    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.product_item, parent, false);
        return new CustomerAdaptor.CustomerViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {

        holder.name.setText(products.get(position).getName());
        holder.quantity.setText(Integer.toString(products.get(position).getQuantity()));
        holder.price.setText(Float.toString(products.get(position).getPrice()));
        holder.date.setText(products.get(position).getDate());

    }



    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {

        TextView name, quantity, date, price;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.item_price);
            quantity = itemView.findViewById(R.id.item_quantity);
        }
    }



}
