package com.example.bookstore.Classes;

import java.util.Date;
import java.util.List;

public class Order {

    private String orderKey;
    private String userUID;
    private String orderDate;
    private float totalPrice;
    private String currency;
    private List<ProductOrderInfo> article;
    private String status;

    public Order() {
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductOrderInfo> getArticle() {
        return article;
    }

    public void setArticle(List<ProductOrderInfo> articleName) {
        this.article = articleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }
}
