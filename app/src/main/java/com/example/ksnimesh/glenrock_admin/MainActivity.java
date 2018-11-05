package com.example.ksnimesh.glenrock_admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.signin.SignIn;

public class MainActivity extends AppCompatActivity {

    Button btnReserve,btnFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        btnReserve=(Button)findViewById( R.id.btn_Reservation );
        btnReserve.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                openReserve();
            }
        } );


        btnFeedback=(Button)findViewById( R.id.btn_feedback );
        btnFeedback.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        } );

    }

    public void  openReserve(){
        Intent intent=new Intent( MainActivity.this,com.example.ksnimesh.glenrock_admin.SignIn.class );
        startActivity( intent );

    }


    public void  openFeedback(){
        Intent intent=new Intent( MainActivity.this,Connectivity.class );
        startActivity( intent );

    }

}
