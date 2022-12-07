package com.example.khataregister.UI.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khataregister.UI.Activities.MainActivity;
import com.example.khataregister.Model.Product;
import com.example.khataregister.R;
import com.example.khataregister.Model.User;
import com.example.khataregister.Model.customer;
import com.example.khataregister.Adaptor.customerList;
import com.example.khataregister.service.counterService;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements customerList.ItemClickListener,View.OnClickListener {

    static ArrayList<Product>data = new ArrayList<>();
    static ArrayList<customer> customersList = new ArrayList<customer>();
    RecyclerView recyclerView;
    customerList adaptor;
    private WifiManager wifiManager;
    private final int GALLERY_REQ_CODE=1000;
    TextView name,sales;
    static TextView receivables;
    EditText newCustomer;
    static String customerName, customerReceivables , customerImg;
    String userName, userSales, userReceivables;
    static int id;
    private static final String TAG=MainActivity.class.getSimpleName();
    private View buttonStartService;
    private Context mContext;
    private Intent serviceIntent;
    private Switch wifiSwitch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customersList = MainActivity.userObj.getCustomers()==null? new ArrayList<customer>(): MainActivity.userObj.getCustomers();
        userName = MainActivity.userObj.getUserName();
        userSales = Integer.toString(MainActivity.userObj.getTotalSales());
        userReceivables = Float.toString(MainActivity.userObj.getReceivables());
        if(customersList!=null && customersList.size()>0)
            populateData(customersList.get(0));
    }

    public static void updateReceivables()
    {
        receivables.setText(Float.toString(MainActivity.userObj.getReceivables()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);
        name = (TextView) root.findViewById(R.id.userName);
        sales = (TextView) root.findViewById(R.id.totalSales);
        receivables = (TextView) root.findViewById(R.id.totalReceivables);
        newCustomer = root.findViewById(R.id.newCustomerName);
        name.setText(userName);
        sales.setText(userSales);
        receivables.setText(userReceivables);

        recyclerView=(RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptor =  new customerList(customersList, getContext(), this);
        recyclerView.setAdapter(adaptor);

        wifiSwitchHandler(root);
        serviceHandler(root);

        Button logout = root.findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User.saveDataToFireBase(getContext());
                User.truncateDb(getContext());

                getActivity().finish();
                // on below line we are exiting our activity
                System.exit(0);

                MainActivity.userObj = null;
                User.db = null;
                Intent intent=new Intent(getContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        Button AddCustomer = root.findViewById(R.id.addCustomer);
        AddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customer cust = new customer(newCustomer.getText().toString());
                customer.addCustomer(cust, getContext());
                Toast.makeText(getContext(), "Customer Added Successfully", Toast.LENGTH_LONG).show();
                newCustomer.setText("");
                MainActivity.userObj.getCustomers().add(cust);
                adaptor.setArrays(customersList);
                adaptor.notifyDataSetChanged();
            }
        });
        return root;
    }

    public void populateData(customer obj) {

        data = obj.getProducts();
        customerName = obj.getName();
        customerReceivables = Float.toString(obj.getReceivable());
        customerImg = obj.getPic();
        id = obj.getId();
    }


    @Override
    public void onItemClick(int pos) {
        customer obj = this.customersList.get(pos);
        populateData(obj);
        int index = MainProfile.viewPager.getCurrentItem()+1;

        MainProfile.viewPager.setCurrentItem(index,true);
        DetailsFragment frag = (DetailsFragment) MainProfile.adapter.createFragment(index);
        frag.refresh();
    }


    private void serviceHandler(View root) {
        mContext = getContext().getApplicationContext();
        buttonStartService = (View) root.findViewById(R.id.buttonStartService);

        buttonStartService.setOnClickListener(this);
        serviceIntent = new Intent(getContext(), counterService.class);
        getActivity().startService(serviceIntent);

    }
    @Override
    public void onClick (View view){
        Toast.makeText(mContext, Integer.toString(counterService.getRandomNumber()) + " seconds used", Toast.LENGTH_SHORT).show();
    }



    private void wifiSwitchHandler(View root) {
        wifiSwitch = root.findViewById(R.id.internetSwitch);
        wifiManager =(WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    wifiManager.setWifiEnabled(true);
                    //wifiSwitch.setText("Wifi is ON");
                }else{
                    wifiManager.setWifiEnabled(false);
                    //wifiSwitch.setText("Wifi is OFF");
                }
            }
        });

        if(wifiManager.isWifiEnabled()){
            wifiSwitch.setChecked(true);
            //wifiSwitch.setText("Wifi is ON");
        }else
        {
            wifiSwitch.setChecked(false);
           // wifiSwitch.setText("Wifi is OFF");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        getContext().getApplicationContext().registerReceiver(wifiStateReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(wifiStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    wifiSwitch.setChecked(true);
                    //wifiSwitch.setText("WiFi is ON");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    wifiSwitch.setChecked(false);
                    //wifiSwitch.setText("WiFi is OFF");
                    break;
            }
        }
    };

}