package com.example.bookstore.Classes;

public class CardData {

    private String cardNumber;
    private String expireDate;
    private String cvv;
    private String CardHolderName;

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
}
