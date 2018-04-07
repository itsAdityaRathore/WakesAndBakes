package com.example.mypc.wakesandbakes.Model;

/**
 * Created by MY PC on 18-Feb-18.
 */

public class Category {

    private String Name;
    private String Image;

    public Category(){

    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
    }

    public Category(String name) {
        Name = name;
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
