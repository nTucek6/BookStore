package com.example.bookstore.Classes;

public class ShoppingCart {

    private String productId;
    private String productType;
    private int quantityToBuy;
    private String userUID;

    public ShoppingCart(){}

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getQuantityToBuy() {
        return quantityToBuy;
    }

    public void setQuantityToBuy(int quantityToBuy) {
        this.quantityToBuy = quantityToBuy;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
