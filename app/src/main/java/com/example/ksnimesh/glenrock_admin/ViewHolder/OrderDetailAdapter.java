package com.example.ksnimesh.glenrock_admin.ViewHolder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ksnimesh.glenrock_admin.Model.Order;
import com.example.ksnimesh.glenrock_admin.R;

import java.util.List;

class MyviewHolder extends RecyclerView.ViewHolder{

public TextView name,quantity,price,discount;


    public MyviewHolder(@NonNull View itemView) {
        super( itemView );
        name= (TextView)itemView.findViewById( R.id.product_name );
        quantity= (TextView)itemView.findViewById( R.id.product_quantity );
        price= (TextView)itemView.findViewById( R.id.Product_price );
        discount= (TextView)itemView.findViewById( R.id.product_discount );
    }
}


public class OrderDetailAdapter extends RecyclerView.Adapter<MyviewHolder>  {

    List<Order>myOrders;

    public OrderDetailAdapter(List<Order>myOrders){
        this.myOrders=myOrders;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup ,int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext() )
                .inflate( R.layout.order_detail_layout,viewGroup,false );
        return new  MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder ,int i) {
        Order order =myOrders.get( i );
        myviewHolder.name.setText( String.format( "Name : %s ",order.getProductName() ));
        myviewHolder.quantity.setText( String.format( "Qunatity : %s ",order.getDiscount() ));
        myviewHolder.price.setText( String.format( "Price : %s ",order.getPrice() ));
        myviewHolder.discount.setText( String.format( "Discount : %s ",order.getQuantity() ));


    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
