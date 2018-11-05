package com.example.ksnimesh.glenrock_admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ksnimesh.glenrock_admin.Common.Common;
import com.example.ksnimesh.glenrock_admin.Model.Cateogry;
import com.example.ksnimesh.glenrock_admin.Model.Foods;
import com.example.ksnimesh.glenrock_admin.ViewHolder.FoodViewHolder;
import com.example.ksnimesh.glenrock_admin.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodList extends AppCompatActivity {
    //View
    RecyclerView recyler_menu;
    RecyclerView.LayoutManager LayoutManager;
    FloatingActionButton fab;

    RelativeLayout rootLayout;


    FirebaseDatabase db;
    DatabaseReference foodList;

    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";
    FirebaseRecyclerAdapter<Foods,FoodViewHolder> adapter;

    //Add new Foods
    EditText edtNames,edtDescriptions,edtPrices,edtDiscounts;
    Button btnSelect,btnUpload;
   // private  final int PICK_IMAGE_REQUEST = 71;
    Foods newFood;
    Uri saveUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_food_list );


        //init firebase;
        db= FirebaseDatabase.getInstance();
        foodList=db.getReference("Foods");
        storage =FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        //Init View
        recyler_menu=(RecyclerView)findViewById( R.id.Recycler_food );
        recyler_menu.setHasFixedSize( true );
        LayoutManager=new LinearLayoutManager( this );
        recyler_menu.setLayoutManager( LayoutManager );

        rootLayout= (RelativeLayout)findViewById( R.id.rootlayout );

    fab= (FloatingActionButton)findViewById( R.id.fab );
    fab.setOnClickListener( new View.OnClickListener( ) {
        @Override
        public void onClick(View v) {
         showAddFoodDialog();
        }
    } );

        if (getIntent ()!=null){
            categoryId=getIntent ().getStringExtra ( "CategoryId" );
            if (!categoryId.isEmpty ()&& categoryId!=null){

                loadListFood(categoryId);
            }

        }


    }

    //popup input shows
    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder( FoodList.this );
        alertDialog.setTitle( "add new Foods" );
        alertDialog.setMessage( "Please Fill Full Information" );

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate( R.layout.add_nav_food_layout,null );

        edtNames= add_menu_layout.findViewById( R.id.edtName );
        edtDescriptions= add_menu_layout.findViewById( R.id.edtDescription );
        edtPrices= add_menu_layout.findViewById( R.id.edtPrice );
        edtDiscounts= add_menu_layout.findViewById( R.id.edtDiscount );


        btnSelect=add_menu_layout.findViewById( R.id.btnSelect );
        btnUpload=add_menu_layout.findViewById( R.id.btnUpload );

        //Eevent for button

        btnSelect.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                chooseImage();// select image in gallery
            }
        } );

        btnUpload.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        } );




        alertDialog.setView( add_menu_layout );
        alertDialog.setIcon( R.drawable.ic_shopping_cart_black_24dp );

        alertDialog.setPositiveButton( "Yes" ,new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog ,int which) {
                dialog.dismiss();

                //Here,Just create new Category
                if(newFood != null){

                    foodList.push().setValue( newFood );
                    Snackbar.make(rootLayout ,"New category "+newFood.getName()+" was added" ,Snackbar.LENGTH_SHORT ).show( );
                }

            }
        } );
        alertDialog.setNegativeButton( "NO" ,new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog ,int which) {
                dialog.dismiss();
            }
        } );
        alertDialog.show();

    }

    //load foodList
    private void loadListFood(String categoryId) {

        adapter  = new FirebaseRecyclerAdapter <Foods, FoodViewHolder> ( Foods.class ,
                R.layout.food_item ,
                FoodViewHolder.class ,
                foodList.orderByChild ( "menuId" ).equalTo ( categoryId ) ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder , Foods model , int i) {
                viewHolder.food_name.setText ( model.getName () );
                Picasso.with ( getBaseContext ()).load(model.getImage () )
                        .into ( viewHolder.food_image );



                viewHolder.setItemClickListener( new AdapterView.OnItemClickListener () {
                    @Override
                    public void onItemClick(AdapterView <?> adapterView , View view , int i , long id) {

                    }


                } );


            }




        };
        adapter.notifyDataSetChanged();
        recyler_menu.setAdapter ( adapter );

    }

    //imageUpload
    private void uploadImage() {
        if(saveUri != null){

            final ProgressDialog mDialog = new ProgressDialog( this );
            mDialog.setMessage( "Uploading..." );
            mDialog.show();

            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder= storageReference.child( "images/"+imageName );
            imageFolder.putFile( saveUri )
                    .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText( FoodList.this ,"Uploaded..!" ,Toast.LENGTH_SHORT ).show( );
                            imageFolder.getDownloadUrl().addOnSuccessListener( new OnSuccessListener <Uri>( ) {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for newcategory if image and we can download link
                                    newFood = new Foods();
                                    newFood.setName( edtNames.getText().toString() );
                                    newFood.setDescription( edtDescriptions.getText().toString() );
                                    newFood.setPrice( edtPrices.getText().toString() );
                                    newFood.setDiscount( edtDiscounts.getText().toString() );
                                    newFood.setMenuId(categoryId);
                                    newFood.setImage( uri.toString() );

                                }
                            } );

                        }
                    } ).addOnFailureListener( new OnFailureListener( ) {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText( FoodList.this ," "+e.getMessage() ,Toast.LENGTH_SHORT ) .show( );

                }
            } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>( ) {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress =(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage( "Uploading "+progress+" %" );
                }
            } );

        }


    }

    //Image choose
    private void chooseImage() {

        Intent intent =new Intent( );
        intent.setType( "image/*" );
        intent.setAction( intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent,"Select Picture" ),Common.PICK_IMAGE_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode ,int resultCode ,@Nullable Intent data) {
        super.onActivityResult( requestCode ,resultCode ,data );
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode== RESULT_OK
                && data != null && data.getData() !=null){

            saveUri=data.getData();
            btnSelect.setText( "Image Selected" );


        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals( Common.UPDATE )){

            showUpdateDoodDialog(adapter.getRef( item.getOrder()).getKey(),adapter.getItem( item.getOrder() ));
        }

        else if (item.getTitle().equals( Common.DELETE )){
            
            deletFood(adapter.getRef( item.getOrder() ).getKey());
        }


        return super.onContextItemSelected( item );
    }

    private void deletFood(String key) {
        foodList.child( key ).removeValue();
        adapter.notifyDataSetChanged();
    }

    private void showUpdateDoodDialog(final String key ,final Foods item) {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder( FoodList.this );
        alertDialog.setTitle( "Edit Food" );
        alertDialog.setMessage( "Please Fill Full Information" );

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate( R.layout.add_nav_food_layout,null );

        edtNames= add_menu_layout.findViewById( R.id.edtName );
        edtDescriptions= add_menu_layout.findViewById( R.id.edtDescription );
        edtPrices= add_menu_layout.findViewById( R.id.edtPrice );
        edtDiscounts= add_menu_layout.findViewById( R.id.edtDiscount );

        edtNames.setText( item.getName() );
        edtDescriptions.setText( item.getDescription() );
        edtPrices.setText( item.getPrice() );
        edtDiscounts.setText( item.getDiscount() );


        btnSelect=add_menu_layout.findViewById( R.id.btnSelect );
        btnUpload=add_menu_layout.findViewById( R.id.btnUpload );

        //Eevent for button

        btnSelect.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                chooseImage();// select image in gallery
            }
        } );

        btnUpload.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                ChangeImage(item);
            }
        } );


        alertDialog.setView( add_menu_layout );
        alertDialog.setIcon( R.drawable.ic_shopping_cart_black_24dp );

        alertDialog.setPositiveButton( "Yes" ,new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog ,int which) {
                dialog.dismiss();

                //Here,Just create new Category


                    item.setName( edtNames.getText().toString() );
                    item.setDescription( edtDescriptions.getText().toString() );
                    item.setDiscount( edtDiscounts.getText().toString() );
                    item.setPrice( edtPrices.getText().toString() );

                    foodList.child(key).setValue( item );

                    Snackbar.make(rootLayout ," Food "+item.getName()+" was adited" ,Snackbar.LENGTH_SHORT ).show( );


            }
        } );
        alertDialog.setNegativeButton( "NO" ,new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog ,int which) {
                dialog.dismiss();
            }
        } );
        alertDialog.show();
        adapter.notifyDataSetChanged();
    }

//Update activity
    private void ChangeImage(final Foods item) {
        if(saveUri != null){

            final ProgressDialog mDialog = new ProgressDialog( this );
            mDialog.setMessage( "Uploading..." );
            mDialog.show();

            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder= storageReference.child( "images/"+imageName );
            imageFolder.putFile( saveUri )
                    .addOnSuccessListener( new OnSuccessListener <UploadTask.TaskSnapshot>( ) {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText( FoodList.this ,"Uploaded..!" ,Toast.LENGTH_SHORT ).show( );
                            imageFolder.getDownloadUrl().addOnSuccessListener( new OnSuccessListener <Uri>( ) {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for newcategory if image and we can download link
                                    item.setImage( uri.toString() );
                                }
                            } );

                        }
                    } ).addOnFailureListener( new OnFailureListener( ) {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText( FoodList.this ,""+e.getMessage() ,Toast.LENGTH_SHORT ).show( );

                }
            } ).addOnProgressListener( new OnProgressListener <UploadTask.TaskSnapshot>( ) {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress =(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage( "Uploaded"+progress+"%" );
                }
            } );

        }


    }





}

