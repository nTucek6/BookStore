package com.example.bookstore.Classes;

public class ProductOrderInfo {

    private String articleKey;
    private String articleName;
    private int articleQuantity;

    public ProductOrderInfo(){}

    public String getArticleKey() {
        return articleKey;
    }

    public void setArticleKey(String articleKey) {
        this.articleKey = articleKey;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public int getArticleQuantity() {
        return articleQuantity;
    }

    public void setArticleQuantity(int articleQuantity) {
        this.articleQuantity = articleQuantity;
    }
}
