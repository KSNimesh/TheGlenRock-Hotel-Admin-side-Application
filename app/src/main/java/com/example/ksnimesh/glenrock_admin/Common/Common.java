package com.example.ksnimesh.glenrock_admin.Common;

import com.example.ksnimesh.glenrock_admin.Model.Request;
import com.example.ksnimesh.glenrock_admin.Model.User;

public class Common {

    public static final int PICK_IMAGE_REQUEST =71 ;
    public static User currentUser;

    public static Request currentRequest;



    public  static final String UPDATE ="Update";
    public  static final String DELETE ="Delete";

//    private static final int PICK_IMAGE_REQUEST = 71;

    public static String convertCodetoStatus(String code){

        if(code.equals( "0" ))
            {return "Placed";}
        else if(code.equals( "1" ))
            {return "On My Way";}

            else {return "Issued Your Order";}
    }

}
