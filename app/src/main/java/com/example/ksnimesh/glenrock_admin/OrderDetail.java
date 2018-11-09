package com.example.ksnimesh.glenrock_admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.ksnimesh.glenrock_admin.Common.Common;
import com.example.ksnimesh.glenrock_admin.ViewHolder.OrderDetailAdapter;

public class OrderDetail extends AppCompatActivity {
    TextView order_id,order_pckage,order_total,order_room,customer;
    String order_id_value="";
    RecyclerView lstFood;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_detail );

        order_id = (TextView) findViewById( R.id.order_id );
        order_pckage = (TextView) findViewById( R.id.order_packageNo );
        order_total = (TextView) findViewById( R.id.order_total );
        customer = (TextView) findViewById( R.id.Customer_name );


        lstFood = (RecyclerView) findViewById( R.id.lstFoods );
        lstFood.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this );
        lstFood.setLayoutManager( layoutManager );

        if (getIntent( ) != null) {
            order_id_value = getIntent( ).getStringExtra( "OrderId" );

            //Set value
            order_id.setText( order_id_value );
            order_pckage.setText( Common.currentRequest.getPackageNo( ) );
            order_total.setText( Common.currentRequest.getTotal( ) );
            customer.setText( Common.currentRequest.getCustomer_Name( ) );
       //     order_room.setText( Common.currentRequest.getAddress( ) );


            OrderDetailAdapter adapter = new OrderDetailAdapter( Common.currentRequest.getFoods( ) );
            adapter.notifyDataSetChanged( );
            lstFood.setAdapter( adapter );


        }
    }
}
