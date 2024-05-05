package com.wychlw.watertime;

public class Drink {
    final private String name;
    final private int imageId;
    public Drink(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
