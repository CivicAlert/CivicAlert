package com.example.civicalertoriginal.Components;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
     FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference myRef = database.getReference("Community Members");
    String name;
    String surname;
    String email;
    String password;
    String phone;

    public void getUserDetail( String name, String surname, String email,String password, String phone){
        this.name =name;
        this.surname=surname;
        this.email = email;
        this.phone = phone;
        this.password=password;
    }
    public void saveUser(){
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name" , this.name);
        newUser.put("surname", this.surname);
        newUser.put("email" ,this.email);
        newUser.put("password" ,this.password);
        newUser.put("phoneNumber" , this.phone);

        String userId = myRef.push().getKey();
        myRef.child(userId).setValue(newUser);
    }

    public void saveUserByEmail(Context context){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( task -> {
            if (task.isSuccessful()){
                Log.d(TAG,"User created with the email");
                FirebaseUser user = auth.getCurrentUser();
                saveUser();
            }});
    }
}
