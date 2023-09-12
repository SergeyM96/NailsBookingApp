package com.example.bayannails.Pages;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.example.bayannails.Classes.User;
import com.example.bayannails.Classes.Order;
import com.example.bayannails.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

public class SignUp_activity extends AppCompatActivity {

    private EditText etName, etFamilyName, etEmail, etPhone, etPassword, etUser_Name;
    private Button btnSignUp;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("התחברות");

        etName = findViewById(R.id.etName);
        etFamilyName = findViewById(R.id.etFamilyName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
        etUser_Name = findViewById(R.id.etUserName);
        userRef = FirebaseDatabase.getInstance().getReference("users");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the inputs
                if (validateInputs()) {
                    // Navigate to MainPage activity
                    startActivity(new Intent(SignUp_activity.this, MainPage.class));
                    finish(); // Finish the current activity if navigation is successful
                }
            }
        });
    }

    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String familyName = etFamilyName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String userName = etUser_Name.getText().toString().trim();
        // Check if any input is empty
        if (TextUtils.isEmpty(name)) {
            etName.setError("נא הכנס שם פרטי");
            etName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(familyName)) {
            etFamilyName.setError("נא הכנס שם משפחה ");
            etFamilyName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("נא הכנס כתובת דואר אלקטרוני");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("נא הכנס מספר נייד");
            etPhone.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("נא הכנס סיסמה ");
            etPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userName)) {
            etUser_Name.setError("נא בחר שם משתמש");
            etPassword.requestFocus();
            return false;
        }

        // Check if email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(" נא הכנס איימיל תקין");
            etEmail.requestFocus();
            return false;
        }

        // Check if phone number is valid (e.g., contains only digits and has a certain length)
        if (!TextUtils.isDigitsOnly(phone) || phone.length() != 10) {
            etPhone.setError("נא הכנס מספר נייד תקין ");
            etPhone.requestFocus();
            return false;
        }

        // Check if email already exists in Firebase
        Query emailQuery = userRef.orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    etEmail.setError("דואר אלקטרוני קיים כבר במערכת ");
                    etEmail.requestFocus();
                } else {
                    // Email does not exist, continue with other validations

                    // Check if phone number already exists in Firebase
                    Query phoneQuery = userRef.orderByChild("phoneNumber").equalTo(phone);
                    phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                etPhone.setError("מספר נייד קיים כבר במערכת ");
                                etPhone.requestFocus();
                            } else {
                                Query userNameQuery = userRef.orderByChild("userName").equalTo(userName);
                                userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            etUser_Name.setError("שם משתמש קיים כבר במערכת ");
                                            etUser_Name.requestFocus();
                                        } else {
                                            List<Order> orderList = new ArrayList<>(); // Create an empty order list
                                            User user = new User(name, familyName, email, password, phone, userName, orderList);
                                            String userId = userRef.push().getKey();
                                            userRef.child(userId).setValue(user);

                                            Toast.makeText(SignUp_activity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUp_activity.this, MainPage.class));
                                            finish(); // Finish the current activity if navigation is successful
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle database error
                                        Toast.makeText(SignUp_activity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle database error
                            Toast.makeText(SignUp_activity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(SignUp_activity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }
}
