package com.example.ksnimesh.glenrock_admin.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.ksnimesh.glenrock_admin.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,
        View.OnLongClickListener
        ,View.OnCreateContextMenuListener{
    public TextView txt_OrderId;
    public TextView txt_OrdeeStatus;
    public TextView txt_OrderPackage;
    public TextView txt_OrderAdress;

    private AdapterView.OnItemClickListener itemClickListener;




    public OrderViewHolder(@NonNull View itemView) {
        super( itemView );
        txt_OrderId= (TextView)itemView.findViewById( R.id.order_id );
        txt_OrderAdress= (TextView)itemView.findViewById( R.id.order_address );
        txt_OrdeeStatus= (TextView)itemView.findViewById( R.id.order_status );
        txt_OrderPackage= (TextView)itemView.findViewById( R.id.order_package );

        itemView.setOnClickListener( this );
        itemView.setOnCreateContextMenuListener( this );


    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu ,View v ,ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle( "Select The Action" );
            menu.add( 0,0, getAdapterPosition(),"Update" );
        menu.add( 0,1, getAdapterPosition(),"Delete" );

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onItemClick(null, v, getAdapterPosition(), v.getId());

        return true;
    }
}
