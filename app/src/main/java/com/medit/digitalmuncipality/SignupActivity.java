package com.medit.digitalmuncipality;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    EditText userName, userPass, userPhone;
    Button signUpBtn;
    DatabaseReference myRef;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        if (pref.getBoolean("isLoggedIn",false)){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        TextView userAlreadyRegistered = findViewById(R.id.user_registered);
        userAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        userName = findViewById(R.id.sign_user_name);
        userPass = findViewById(R.id.sign_pass);
        userPhone = findViewById(R.id.sign_phone);

        signUpBtn = findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = userName.getText().toString();
                String pass = userPass.getText().toString();
                String phone = userPhone.getText().toString();

                if (!name.isEmpty() && !pass.isEmpty() && !phone.isEmpty()) {

                    RegisterationModel user = new RegisterationModel(name, pass, phone);
                    isPhoneRegistered(phone, user);
                }else{
                    Toast.makeText(SignupActivity.this, "One of the feild is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registedUser(final RegisterationModel user) {
        myRef.child(user.getPhone()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...
                Toast.makeText(SignupActivity.this, "Welcome"+ user.getName(), Toast.LENGTH_SHORT).show();
                editor.putBoolean("isLoggedIn",true);
                editor.putString("phone",user.getPhone());
                editor.commit();
                startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });


    }


    public void isPhoneRegistered(final String phone, final RegisterationModel user){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(phone)) {
                    // run some code
                    Toast.makeText(SignupActivity.this, "Phone Number Already in use", Toast.LENGTH_SHORT).show();
                }else{
                    registedUser(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
