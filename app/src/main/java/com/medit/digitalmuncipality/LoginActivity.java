package com.medit.digitalmuncipality;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView userUnregister;
    DatabaseReference myRef;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        if (pref.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        final EditText phone, pass;

        phone = findViewById(R.id.login_phone);
        pass = findViewById(R.id.login_pass);

        Button login;
        login = findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ph = phone.getText().toString();
                String pas = pass.getText().toString();
                if (!ph.isEmpty() && !pas.isEmpty()) {
                    isPhoneRegistered(ph, pas);
                } else {
                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        userUnregister = findViewById(R.id.user_unregistered);
        userUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    public void registerUser() {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void isPhoneRegistered(final String phone, final String pass) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(phone)) {
                    // run some code
                    DatabaseReference childRef = myRef.child(phone);
                    childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            RegisterationModel model = dataSnapshot.getValue(RegisterationModel.class);
                            if (pass.equals(model.getPass())) {
//                                Toast.makeText(LoginActivity.this, "Pass" + pass + "get "+ model.getPass(), Toast.LENGTH_SHORT).show();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("phone", phone);
                                editor.commit();
                                //move to home
                                Toast.makeText(LoginActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "User is not registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
