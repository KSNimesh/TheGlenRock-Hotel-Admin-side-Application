package com.example.ksnimesh.glenrock_admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.ksnimesh.glenrock_admin.Common.Common;
import com.example.ksnimesh.glenrock_admin.Model.Order;
import com.example.ksnimesh.glenrock_admin.Model.Request;
import com.example.ksnimesh.glenrock_admin.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference request;
    MaterialSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_status );

        db = FirebaseDatabase.getInstance();
        request = db.getReference("Requests");

        recyclerView=(RecyclerView)findViewById( R.id.listOrders );
        recyclerView.setHasFixedSize( true );
        layoutManager= new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        
        loadOrders();
    }

    private void loadOrders() {

        adapter= new FirebaseRecyclerAdapter <Request, OrderViewHolder>(
                Request.class ,
                R.layout.order_layout ,
                OrderViewHolder.class ,
                request ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder ,final Request model ,int position) {
                viewHolder.txt_OrderId.setText( adapter.getRef( position ).getKey() );
                viewHolder.txt_OrdeeStatus.setText( Common.convertCodetoStatus( model.getStatus() ));
                viewHolder.txt_OrderAdress.setText( model.getAddress() );
                viewHolder.txt_OrderPackage.setText( model.getPackageNo() );

                viewHolder.setItemClickListener( new AdapterView.OnItemClickListener( ) {
                    @Override
                    public void onItemClick(AdapterView <?> parent ,View view ,int position ,long id) {

                       Intent orderDetail = new Intent( OrderStatus.this,OrderDetail.class );
                       Common.currentRequest=model;
                       orderDetail.putExtra( "OrderId",adapter.getRef( position ).getKey() );
                       startActivity( orderDetail );



                    }
                } );
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter( adapter );
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals( Common.UPDATE )){

            showUpdateDialoag(adapter.getRef( item.getOrder() ).getKey(),adapter.getItem( item.getOrder() ));
            }

            else if(item.getTitle().equals( Common.DELETE )){
            deleteOrder(adapter.getRef( item.getOrder() ).getKey());
        }

        return super.onContextItemSelected( item );
    }

    private void deleteOrder(String key) {
        request.child( key ).removeValue();
    }

    private void showUpdateDialoag(String key ,final Request item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder( OrderStatus.this );
        alertDialog.setTitle( "Update Order" ) ;
        alertDialog.setMessage( "Please choose status" );

        LayoutInflater inflater = this.getLayoutInflater();
        final View view =inflater.inflate(R.layout.update_order_layout,null);

        spinner = (MaterialSpinner)view.findViewById( R.id.statusSpinner );
        spinner.setItems( "Placed","On My Way","Issued Your Order" );
        alertDialog.setView( view );

        final  String localKey=key;
        alertDialog.setPositiveButton( "Yes" ,new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog ,int which) {
                dialog.dismiss();
                item.setStatus( String.valueOf( spinner.getSelectedIndex() ) );
                request.child( localKey ).setValue(item  );


            }
        } );

        alertDialog.setNegativeButton( "No" ,new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog ,int which) {
                dialog.dismiss();
            }
        } );
        alertDialog.show();

    }
}
