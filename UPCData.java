package com.example.kurtiscc.upccatalog;

/**
 * Created by kurtiscc on 2/7/2015.
 */
public class UPCData {

    private String upccode, productname, image;
    private int id;

    public UPCData(){
        this.id = 0;
        this.upccode = "";
        this.productname = "";
        this.image = "";
    }

    public void setUpccode(String upccode) {
        this.upccode = upccode;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpccode() {
        return upccode;
    }

    public String getProductname() {
        return productname;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

}
