package com.wielabs.groupchat;

public class ShoppingItem {

    String id,title,image1,image2,image3,mrp,sp,stock,size,color,barcode;

    public ShoppingItem(String id,String title,String image1,String image2,String image3,String mrp,String sp,String stock,String size,String color,String barcode){
        this.id = id;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.title = title;
        this.mrp = mrp;
        this.sp = sp;
        this.stock = stock;
        this.size = size;
        this.color = color;
        this.barcode = barcode;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getColor() {
        return color;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getMrp() {
        return mrp;
    }

    public String getSize() {
        return size;
    }

    public String getSp() {
        return sp;
    }

    public String getStock() {
        return stock;
    }
}
