package com.example.ksnimesh.glenrock_admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ksnimesh.glenrock_admin.Common.Common;
import com.example.ksnimesh.glenrock_admin.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    EditText edtPacakage_No,edtPassword_No;
    Button btnSignIn;

        FirebaseDatabase db;
        DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_in );


        edtPassword_No= (EditText)findViewById(R.id.Password_No);
        edtPacakage_No= (EditText)findViewById(R.id.Package_No);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);



        db= FirebaseDatabase.getInstance();
        users=db.getReference("User");

        btnSignIn.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                SignInUser(edtPacakage_No.getText().toString(),edtPassword_No.getText().toString());
                
            }
        } );
    }

    private void SignInUser(String package_No ,String password) {
        final ProgressDialog mDialog=new ProgressDialog( SignIn.this );
        mDialog.setMessage( "Please Waiting..." );
        mDialog.show();

        final String localPackage= package_No;
        final  String localPassword= password;

        users.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child( localPackage ).exists( )) {
                    mDialog.dismiss( );

                    User user = dataSnapshot.child(localPackage).getValue(User.class);
                    user.setPackage_NO( localPackage );

                    if (Boolean.parseBoolean( user.getIsStaff() )) //if isStaff=true
                    {

                        if (user.getPassword( ).equals( localPassword )) {

                            Intent login=new Intent( SignIn.this,Home.class );
                            Common.currentUser=user;
                            startActivity( login );
                            finish();


                        } else {
                            Toast.makeText( SignIn.this ,"Wrong Password..!" ,Toast.LENGTH_SHORT ).show( ); }
                    }
                    else{ Toast.makeText( SignIn.this,"Please Login With Admin Account ",Toast.LENGTH_SHORT ).show();}

                }
                else {
                    mDialog.dismiss();
                    Toast.makeText( SignIn.this,"Not exist in database ",Toast.LENGTH_SHORT ).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
