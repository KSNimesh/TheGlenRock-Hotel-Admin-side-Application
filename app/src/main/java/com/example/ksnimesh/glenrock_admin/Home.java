package com.example.ksnimesh.glenrock_admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ksnimesh.glenrock_admin.Common.Common;
import com.example.ksnimesh.glenrock_admin.Model.Cateogry;
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

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView  txtRoomNumber;
    FirebaseDatabase database;
    DatabaseReference categories;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<Cateogry,MenuViewHolder>adapter;

    //View
    RecyclerView recyler_menu;
    RecyclerView.LayoutManager LayoutManager;

    //add new menu layout
    EditText edtName;
    Button btnUpload,btnSelect;

    Cateogry newCategory;
    Uri saveUri;

   // private  final int PICK_IMAGE_REQUEST = 71;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setTitle( "Menu Management" );
        setSupportActionBar( toolbar );


        //init Firebase
        database= FirebaseDatabase.getInstance();
        categories=database.getReference("Category");
        storage =FirebaseStorage.getInstance();
        storageReference=storage.getReference();




        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                showDialog( );
            }
        } );

         drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this ,drawer ,toolbar ,R.string.navigation_drawer_open ,R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState( );

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        View headerView =navigationView.getHeaderView( 0 );
        txtRoomNumber=(TextView)headerView.findViewById( R.id.textroomno );
        txtRoomNumber.setText( Common.currentUser.getRoom_NO() );

        //Init View
        recyler_menu=(RecyclerView)findViewById( R.id.recycler_menu );
        recyler_menu.setHasFixedSize( true );
        LayoutManager=new LinearLayoutManager( this );
        recyler_menu.setLayoutManager( LayoutManager );
        
        LoadMenu();
        
    }

    private void showDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder( Home.this );
        alertDialog.setTitle( "add new Category" );
        alertDialog.setMessage( "Please Fill Full Information" );

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate( R.layout.add_new_menu_layout,null );

        edtName=add_menu_layout.findViewById( R.id.edtName );
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
                if(newCategory != null){

                    categories.push().setValue( newCategory );
                    Snackbar.make(drawer ,"New category "+newCategory.getName()+" was added" ,Snackbar.LENGTH_SHORT ).show( );
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

    private void uploadImage() {
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
                            Toast.makeText( Home.this ,"Uploaded..!" ,Toast.LENGTH_SHORT ).show( );
                            imageFolder.getDownloadUrl().addOnSuccessListener( new OnSuccessListener <Uri>( ) {
                                @Override
                                public void onSuccess(Uri uri) {
                                   //set value for newcategory if image and we can download link
                                    newCategory = new Cateogry( edtName.getText().toString(),uri.toString() );

                                }
                            } );

                        }
                    } ).addOnFailureListener( new OnFailureListener( ) {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText( Home.this ," "+e.getMessage() ,Toast.LENGTH_SHORT ).show( );

                }
            } ).addOnProgressListener( new OnProgressListener <UploadTask.TaskSnapshot>( ) {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress =(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage( "Uploading "+progress+"%" );
                }
            } );

        }


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

    private void chooseImage() {

        Intent intent =new Intent( );
        intent.setType( "image/*" );
        intent.setAction( intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent,"Select Picture" ),Common.PICK_IMAGE_REQUEST );
    }

    private void LoadMenu() {

        adapter = new FirebaseRecyclerAdapter <Cateogry, MenuViewHolder>( Cateogry.class ,R.layout.menu_item ,MenuViewHolder.class ,categories ) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder ,Cateogry model ,int position) {
                viewHolder.txtMenuName.setText( model.getName() );
                Picasso.with( getBaseContext() ).load( model.getImage()).into( viewHolder.photoView);

                viewHolder.setItemClickListener( new AdapterView.OnItemClickListener( ) {
                    @Override
                    public void onItemClick(AdapterView <?> parent ,View view ,int position ,long id) {
                               Intent foodList =new Intent( Home.this,FoodList.class );

                        foodList.putExtra("CategoryId",adapter.getRef( position ).getKey());
                        startActivity( foodList );
                    }
                } );
            }
        };

        adapter.notifyDataSetChanged();//refresh
        recyler_menu.setAdapter( adapter );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed( );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater( ).inflate( R.menu.home ,menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId( );

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId( );

        if (id == R.id.nav_order) {

            Intent order= new Intent( Home.this,OrderStatus.class );
            startActivity( order );

            // Handle the camera action
        }
        //else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    //Update /delete button function

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals( (Common.UPDATE) )){
            showUpdateDialog(adapter.getRef( item.getOrder() ).getKey(),adapter.getItem( item.getOrder() ));
        }

        else if(item.getTitle().equals( (Common.DELETE) )){
            deleteCategory(adapter.getRef( item.getOrder() ).getKey());
        }

        return super.onContextItemSelected( item );
    }

    private void deleteCategory(String key) {
        categories.child( key ).removeValue();
        Toast.makeText( this ,"Item Deleted" ,Toast.LENGTH_SHORT ).show( );
    }



    private void showUpdateDialog(final String key ,final Cateogry item) {

            AlertDialog.Builder alertDialog=new AlertDialog.Builder( Home.this );
            alertDialog.setTitle( "Update Category" );
            alertDialog.setMessage( "Please Fill Full Information" );

            LayoutInflater inflater=this.getLayoutInflater();
            View add_menu_layout=inflater.inflate( R.layout.add_new_menu_layout,null );

            edtName=add_menu_layout.findViewById( R.id.edtName );
            btnSelect=add_menu_layout.findViewById( R.id.btnSelect );
            btnUpload=add_menu_layout.findViewById( R.id.btnUpload );

            //set default name
        edtName.setText( item.getName() );

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

                   //update informationn
                   item.setName( edtName.getText().toString() );
                   categories.child( key ).setValue( item );


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

    private void ChangeImage(final Cateogry item) {
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
                            Toast.makeText( Home.this ,"Uploaded..!" ,Toast.LENGTH_SHORT ).show( );
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
                    Toast.makeText( Home.this ,""+e.getMessage() ,Toast.LENGTH_SHORT ).show( );

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
