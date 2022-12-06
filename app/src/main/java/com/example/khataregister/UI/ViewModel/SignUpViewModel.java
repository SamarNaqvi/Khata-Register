package com.example.khataregister.UI.ViewModel;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.example.khataregister.Model.User;

public class SignUpViewModel extends ViewModel {

    public User data;

    public User getNotes(Bundle savedInstanceState, String key){
        if (data == null){
            if (savedInstanceState == null) {
                data = new User();
            }
            else{
                data = (User) savedInstanceState.get(key);
            }
        }
        return data;
    }
}