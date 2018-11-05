package com.example.ksnimesh.glenrock_admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.oob.SignUp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Reservation extends AppCompatActivity {
    Button btnSignIn, btnSignUp;
    EditText PasswordNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_reservation );

//        btnSignIn=(Button)findViewById(R.id.btnSignIn);
//        btnSignUp=(Button)findViewById(R.id.btnSignUp);
//        PasswordNo= (EditText)findViewById(R.id.Password_No);
//
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent SignUp=new Intent(Reservation.this,com.example.ksnimesh.glenrock_admin.SignUp.class);
//                startActivity(SignUp);
//
//            }
//        });
//
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent SignIn=new Intent(Reservation.this,com.example.ksnimesh.glenrock_admin.SignIn.class);
//                startActivity(SignIn);
//            }
//        });
//
//    }

    }
}
