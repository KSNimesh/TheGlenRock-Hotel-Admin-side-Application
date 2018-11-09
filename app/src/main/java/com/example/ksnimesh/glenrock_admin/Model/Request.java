package com.example.ksnimesh.glenrock_admin.Model;

import java.util.List;

public class Request {
    private String PackageNo;
    private String Address;
    private String Customer_Name;
    private String total;
    private String status;
    private List<Order>foods;


    public Request() {
    }

    public Request(String packageNo ,String address ,String customer_Name ,String total ,List <Order> foods) {
        PackageNo = packageNo;
        Address = address;
        Customer_Name = customer_Name;
        this.total = total;
        this.foods = foods;
        this.status="0";
    }

    public String getStatus() {
        return status;
    }

    public String getPackageNo() {
        return PackageNo;
    }

    public void setPackageNo(String packageNo) {
        PackageNo = packageNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List <Order> getFoods() {
        return foods;
    }

    public void setFoods(List <Order> foods) {
        this.foods = foods;
    }
}
