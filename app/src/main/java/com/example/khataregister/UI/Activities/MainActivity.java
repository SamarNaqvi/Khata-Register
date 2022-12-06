package com.example.khataregister.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.impl.utils.ForceStopRunnable;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import com.example.khataregister.UI.Fragments.MainProfile;
import com.example.khataregister.Model.Product;
import com.example.khataregister.R;
import com.example.khataregister.Model.User;
import com.example.khataregister.Model.customer;
import com.example.khataregister.UI.ViewModel.MainViewModel;
import com.example.khataregister.service.counterService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG=MainActivity.class.getSimpleName();

    private View buttonStartService;
    private Context mContext;

    private Intent serviceIntent;

    User obj = new User();

    EditText email;
    private AdView mAdView;
    EditText password;
    TextView signUp;
    android.widget.Button submitButton;
    public static User userObj;
    private Switch wifiSwitch;
    private WifiManager wifiManager;
    private MainViewModel vm;


    public static void userFound(Context ctx) {
        userObj = User.getUser();
        Product.loadProducts(ctx);
    }

    public static void loadUserProfile(User obj, Context ctx)
    {
        userObj = User.getUser();
        if(userObj!=null) {
            User.saveDataToSql(ctx);
            profileTransition(ctx);
        }
    }

    public static void profileTransition(Context ctx)
    {
        if(MainActivity.userObj!=null)
        {
        Intent intent=new Intent(ctx , MainProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
        }
    }
    public static void userNotFound(Context ctx) {
        if(MainActivity.userObj!=null)
        {
        Toast.makeText(ctx, "Invalid Credentials", Toast.LENGTH_LONG).show();
    }
    }

    public static void loadCustomers(Context ctx) {
        if(MainActivity.userObj!=null)
        {
        customer.loadCustomers(ctx);}
    }

    public static void setCustomersToUsers(Context ctx) {
        if(MainActivity.userObj!=null) {
            User.updateCustomers(ctx);
        }
    }

    public static void setProductsToCustomers(Context ctx) {
        if(MainActivity.userObj!=null)
        {
        customer.updateProducts(ctx);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        vm = new ViewModelProvider(this).get(MainViewModel.class);
        obj = vm.getNotes(savedInstanceState,"user");

        if(userObj==null)
        {
            userObj = obj;
        }

        wifiSwitchHandler();
        serviceHandler();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        User.loadUser(MainActivity.this);
        if(MainActivity.userObj!=null && MainActivity.userObj.getUserName()!=null) {
            profileTransition(MainActivity.this);
        }
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        signUp = findViewById(R.id.signUp);
        email = findViewById(R.id.email);
        password=findViewById(R.id.password);
        submitButton = (android.widget.Button)findViewById(R.id.submit);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                if(userEmail.length()==0)
                {
                    email.setError("Email cannot be empty");
                    email.requestFocus();
                }
                if(userPassword.length()==0)
                {
                    password.setError("Password cannot be empty");
                    password.requestFocus();
                }

                User.getUser(MainActivity.this, userEmail, userPassword);
            }
        });
    }

    private void serviceHandler() {
        mContext = getApplicationContext();
        buttonStartService = (View) findViewById(R.id.buttonStartService);

        buttonStartService.setOnClickListener(this);
        serviceIntent = new Intent(getApplicationContext(), counterService.class);
        startService(serviceIntent);

    }
        @Override
        public void onClick (View view){
            Toast.makeText(mContext, Integer.toString(counterService.getRandomNumber()) + " seconds used", Toast.LENGTH_SHORT).show();
        }



    private void wifiSwitchHandler() {
        wifiSwitch = findViewById(R.id.internetSwitch);
        wifiManager =(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    wifiManager.setWifiEnabled(true);
                    wifiSwitch.setText("Wifi is ON");
                }else{
                    wifiManager.setWifiEnabled(false);
                    wifiSwitch.setText("Wifi is OFF");
                }
            }
        });

        if(wifiManager.isWifiEnabled()){
            wifiSwitch.setChecked(true);
            wifiSwitch.setText("Wifi is ON");
        }else
        {
            wifiSwitch.setChecked(false);
            wifiSwitch.setText("Wifi is OFF");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    wifiSwitch.setChecked(true);
                    wifiSwitch.setText("WiFi is ON");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    wifiSwitch.setChecked(false);
                    wifiSwitch.setText("WiFi is OFF");
                    break;
            }
        }
    };

}
