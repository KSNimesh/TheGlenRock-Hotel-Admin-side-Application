package com.example.ksnimesh.glenrock_admin.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ksnimesh.glenrock_admin.Common.Common;
import com.example.ksnimesh.glenrock_admin.Interface.ItemClickListener;
import com.example.ksnimesh.glenrock_admin.R;

public class MenuViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener,
          View.OnCreateContextMenuListener

{

    public TextView txtMenuName;
    public ImageView photoView;

    private AdapterView.OnItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super( itemView );

        txtMenuName=(TextView)itemView.findViewById( R.id.menu_name);
        photoView=(ImageView)itemView.findViewById( R.id.menu_image );

        itemView.setOnCreateContextMenuListener( this );
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());

    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu ,View v ,ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle( "Select the action" );
        menu.add( 0,0,getAdapterPosition(),Common.UPDATE);
        menu.add( 0,0,getAdapterPosition(),Common.DELETE);
    }
}