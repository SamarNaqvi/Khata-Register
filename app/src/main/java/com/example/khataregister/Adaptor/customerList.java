package com.example.khataregister.Adaptor;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khataregister.R;
import com.example.khataregister.Model.customer;

import java.util.ArrayList;
import java.util.List;

public class customerList extends RecyclerView.Adapter<customerList.ExampleViewHolder> implements Filterable{
    ArrayList<customer>  backup;
    ArrayList<customer>  mExampleList;
    Context context;
    private ItemClickListener listener;

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v, this.listener);
        return evh;
    }

    public customerList(ArrayList<customer> exampleList, Context c, ItemClickListener listener){
        backup = exampleList;
        mExampleList = new ArrayList<>(exampleList);
        context = c;
        this.listener = listener;
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



    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTextView1;
        ItemClickListener listener;
        public ExampleViewHolder(View itemView, ItemClickListener listener){
            super(itemView);
            mTextView1=itemView.findViewById(R.id.name);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition());
        }

    }

    public interface ItemClickListener{
        public void onItemClick(int pos);
    };
}
