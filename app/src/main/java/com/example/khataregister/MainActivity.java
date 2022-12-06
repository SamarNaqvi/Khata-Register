package com.example.khataregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    EditText email;
    private AdView mAdView;
    EditText password;

    TextView signUp;
    android.widget.Button submitButton;
    public static User userObj;
    public static void userFound(Context ctx) {
        Product.loadProducts(ctx);
    }

    public static void loadUserProfile(User obj, Context ctx)
    {
        userObj = User.getUser();
        User.saveDataToSql(ctx);
        profileTransition(ctx);
    }

    public static void profileTransition(Context ctx)
    {
        Intent intent=new Intent(ctx ,MainProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
    public static void userNotFound(Context ctx) {
        Toast.makeText(ctx, "Invalid Credentials", Toast.LENGTH_LONG).show();
    }

    public static void loadCustomers(Context ctx) {
        customer.loadCustomers(ctx);
    }

    public static void setCustomersToUsers(Context ctx) {
        User.updateCustomers(ctx);
    }

    public static void setProductsToCustomers(Context ctx) {
        customer.updateProducts(ctx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Intent intent=new Intent(MainActivity.this,SignupActivity.class);
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
}