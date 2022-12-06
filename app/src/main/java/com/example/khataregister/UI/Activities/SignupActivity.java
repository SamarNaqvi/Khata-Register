package com.example.khataregister.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.khataregister.R;
import com.example.khataregister.UI.Fragments.MainProfile;
import com.example.khataregister.Model.User;
import com.example.khataregister.UI.ViewModel.MainViewModel;
import com.example.khataregister.UI.ViewModel.SignUpViewModel;

public class SignupActivity extends AppCompatActivity {

    EditText userName, userEmail, userPassword, userPasswordConfirm, userSales;
    Button Register;
    String name, email, password, passwordConfirm, sales;
    private SignUpViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        vm = new ViewModelProvider(this).get(SignUpViewModel.class);


        userName = findViewById(R.id.signUp_name);
        userEmail = findViewById(R.id.signUp_email);
        userPassword = findViewById(R.id.signUp_password);
        userSales = findViewById(R.id.signUp_sales);
        userPasswordConfirm = findViewById(R.id.signUp_password_confirm);
        Register = findViewById(R.id.registerUser);


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = userName.getText().toString().trim();
                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();
                passwordConfirm = userPasswordConfirm.getText().toString().trim();
                sales = userSales.getText().toString().trim();


                if(name.length()==0)
                {
                    userName.setError("User name can't be empty");
                    userName.requestFocus();
                    return;
                }

                if(email.length()==0)
                {
                    userEmail.setError("User Email can't be empty");
                    userEmail.requestFocus();
                    return;
                }
                if(password.length()==0)
                {
                    userPassword.setError("User Password can't be empty");
                    userPassword.requestFocus();
                    return;
                }

                if(passwordConfirm.length()==0)
                {
                    userPasswordConfirm.setError("User Password Confirm can't be empty");
                    userPasswordConfirm.requestFocus();
                    return;
                }

                if(sales.length()==0)
                {
                    userSales.setError("User Sales  can't be empty");
                    userSales.requestFocus();
                    return;
                }

                if(!passwordConfirm.equals(password))
                {
                    userPasswordConfirm.setError("User Password doesn't match");
                    userPasswordConfirm.requestFocus();
                    return;
                }
                MainActivity.userObj = new User();
                MainActivity.userObj.setUserName(name);
                MainActivity.userObj.setEmail(email);
                MainActivity.userObj.setPassword(password);
                MainActivity.userObj.setTotalSales(Integer.parseInt(sales));
                User.saveDataToSql(SignupActivity.this);

                Intent intent=new Intent(SignupActivity.this, MainProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }
}