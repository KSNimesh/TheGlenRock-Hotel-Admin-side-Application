package com.example.ksnimesh.glenrock_admin.Model;

public class User {

   private String Package_NO;
   private String IsStaff;
  private   String Password;
  private   String Room_NO;

    public User() {
    }

    public User(String password ,String room_NO) {
        Password = password;
        Room_NO = room_NO;
    }


    public String getPackage_NO() {
        return Package_NO;
    }

    public void setPackage_NO(String package_NO) {
        Package_NO = package_NO;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRoom_NO() {
        return Room_NO;
    }

    public void setRoom_NO(String room_NO) {
        Room_NO = room_NO;
    }
}

