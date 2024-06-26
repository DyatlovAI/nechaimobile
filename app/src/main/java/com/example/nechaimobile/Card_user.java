package com.example.nechaimobile;

public class Card_user {
    private String userId, number, data, cvc;

    public Card_user() {
    }

    public Card_user(String userId, String number, String data, String cvc) {
        this.userId = userId;
        this.number = number;
        this.data = data;
        this.cvc = cvc;
    }
    public String getUserId() {
        return userId;
    }

    public String getNumber() {
        return number;
    }
    public String getData() {
        return data;
    }

    public String getCvc() {
        return cvc;
    }
}
