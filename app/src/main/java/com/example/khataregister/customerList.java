package com.example.khataregister;


import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class customerList extends RecyclerView.Adapter<customerList.ExampleViewHolder> implements Filterable{
    ArrayList<customer>  backup;
    ArrayList<customer>  mExampleList;
    Context context;
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    public customerList(ArrayList<customer> exampleList, Context c){
        backup = exampleList;
        mExampleList = new ArrayList<>(exampleList);
        context = c;
    }

    public customerList( Context c){
        context = c;
    }

    public void setArrays(ArrayList<customer> exampleList)
    {
        backup = exampleList;
        mExampleList = new ArrayList<>(exampleList);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        customer currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem.getName());

        holder.mTextView1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(context,ProductPage.class);
//                intent.putExtra("imagename",currentItem.getImageResource());
//                intent.putExtra("productname",currentItem.getText1());
//                intent.putExtra("price",currentItem.getText2());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<customer> filteredList = new ArrayList<>();
            System.out.println("Yes");
            if(charSequence == null || charSequence.length()==0)
            {
                filteredList.addAll(backup);
            }else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(customer item :backup){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        };

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            mExampleList.clear();
            mExampleList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };



    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public ExampleViewHolder(View itemView){
            super(itemView);
            mTextView1=itemView.findViewById(R.id.name);

        }

    }


}
