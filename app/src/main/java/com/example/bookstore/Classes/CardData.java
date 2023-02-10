package com.example.bookstore.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CardData {

    private String cardNumber;
    private String expireDate;
    private String cvv;
    private String CardHolderName;
    private float balance;
    private  String currency;

    public CardData()
    {}

    public String  getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String  getCvv() {
        return cvv;
    }

    public void setCvv(String  cvv) {
        this.cvv = cvv;
    }

    public String getCardHolderName() {
        return CardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        CardHolderName = cardHolderName;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cardNumber", getCardNumber());
        result.put("expireDate", getExpireDate());
        result.put("cvv", getCvv());
        result.put("CardHolderName", getCardHolderName());
        result.put("balance", getBalance());
        result.put("currency", getCurrency());
        return result;
    }


}
