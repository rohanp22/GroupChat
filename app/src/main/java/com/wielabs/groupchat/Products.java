package com.wielabs.groupchat;

public class Products {

    private String name;
    private String description;
    private String id;
    private String image;

    public Products(String id,String image, String name, String description) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId(){
        return Integer.parseInt(id);
    }

}
