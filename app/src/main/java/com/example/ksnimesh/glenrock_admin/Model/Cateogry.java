package com.example.ksnimesh.glenrock_admin.Model;

public class Cateogry {

    private String Name;
    private String Image;

    public Cateogry() {
    }

    public Cateogry(String name ,String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
