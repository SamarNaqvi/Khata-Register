package com.example.khataregister.UI.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khataregister.Adaptor.CustomerAdaptor;
import com.example.khataregister.Model.Product;
import com.example.khataregister.R;
import com.example.khataregister.Model.customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailsFragment extends Fragment {

    CustomerAdaptor adaptor;
    static ArrayList<Product> data = new ArrayList<Product>();
    ImageView img;
    TextView name, payable;
    static String personName= "sample User", price= "0", imgRsc="user";
    AppCompatButton addProduct, editBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createData();

    }

    public void createData(customer obj) {

        data = ProfileFragment.data==null? new ArrayList<Product>():ProfileFragment.data;
        personName = ProfileFragment.customerName==null? "sample user": ProfileFragment.customerName;
        price =ProfileFragment.customerReceivables=="-1"? "0" : ProfileFragment.customerReceivables;
        imgRsc =ProfileFragment.customerImg==null? "user": ProfileFragment.customerImg;
    }
    public void createData() {

        data = ProfileFragment.data==null? new ArrayList<Product>():ProfileFragment.data;
        personName = ProfileFragment.customerName==null? "sample user": ProfileFragment.customerName;
        price =ProfileFragment.customerReceivables=="-1"? "0" : ProfileFragment.customerReceivables;
        imgRsc =ProfileFragment.customerImg==null? "user": ProfileFragment.customerImg;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.fragment_details, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new CustomerAdaptor(data);

        recyclerView.setAdapter(adaptor);

        name = root.findViewById(R.id.name);
        payable = root.findViewById(R.id.payable);
        img = root.findViewById(R.id.picture);
        addProduct = root.findViewById(R.id.add_new_product);
        editBalance = root.findViewById(R.id.update_receivable);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createData();
                setData();
                adaptor.setArrays(data);
                adaptor.notifyDataSetChanged();
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProductDialog();
            }
        });

        editBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditBalanceDialog();
            }
        });

        setData();

        return root;
    }
    private void setData()
    {
        int rscId = getResources().getIdentifier(imgRsc, "drawable", getActivity().getPackageName());
        img.setImageResource(rscId);

        name.setText(personName);
        payable.setText("$"+ price);
    }
    public void showAddProductDialog()
    {
        AlertDialog.Builder alert;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            alert = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert = new AlertDialog.Builder(getContext());
        }

        LayoutInflater inflater = getLayoutInflater();

        View view =  inflater.inflate(R.layout.product_dialog, null);

        EditText productName = view.findViewById(R.id.productName);

        EditText price = view.findViewById(R.id.productPrice);

        EditText quantity = view.findViewById(R.id.productQuantity);

//        EditText date = view.findViewById(R.id.purchaseDate);

        alert.setView(view);

        alert.setCancelable(false);

        view.findViewById(R.id.addProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                int max = 999999999;
                int min = 0;
                int random_int = data.size() + (int)Math.floor(Math.random()*(max-min+1)+min);
                Product prod = new Product(
                        random_int,
                        productName.getText().toString(),
                        Float.parseFloat(price.getText().toString()),
                        Integer.parseInt(quantity.getText().toString()),
                        date
                        );


                Product.addProduct(prod,getContext(), ProfileFragment.id);

             //   MainActivity.userObj.getCustomers().get(ProfileFragment.id).getProducts().add(prod);
                adaptor.notifyDataSetChanged();
                Toast.makeText(getContext(),"Product Added", Toast.LENGTH_LONG).show();

            }

        });


        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        view.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showEditBalanceDialog()
    {

        AlertDialog.Builder alert;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            alert = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert = new AlertDialog.Builder(getContext());
        }

        LayoutInflater inflater = getLayoutInflater();

        View view =  inflater.inflate(R.layout.edit_payable, null);

        EditText newBalance = view.findViewById(R.id.updated_balance);

        alert.setView(view);

        alert.setCancelable(false);


        view.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payable.setText(newBalance.getText());
                price = newBalance.getText().toString();
                customer.updateBalance(personName, Integer.toString(ProfileFragment.id), price , getContext());
                Toast.makeText(getContext(),"Balance Updated", Toast.LENGTH_LONG).show();
            }

        });

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();


        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void refresh() {
        createData();
    }
}