package com.example.nechaimobile.Models;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Order_Aut {
    private String userID;
    private String number_aut;
    private String name_aut;
    private int price;
    private int time;
    private String photo_aut;
    private String aut_class;
    private String aut_korobka;
    private String aut_latitude;
    private String aut_longitude;
    private String OrderId;
    private String orderDate;

    public Order_Aut() {
    }

    public Order_Aut(String OrderId,String userID, String number_aut, String name_aut, int price, int time, String photo_aut, String aut_class, String aut_korobka, String aut_latitude, String aut_longitude,String orderDate) {
        this.userID = userID;
        this.number_aut = number_aut;
        this.name_aut = name_aut;
        this.price = price;
        this.time = time;
        this.photo_aut = photo_aut;
        this.aut_class = aut_class;
        this.aut_korobka = aut_korobka;
        this.aut_latitude = aut_latitude;
        this.aut_longitude = aut_longitude;
        this.OrderId=OrderId;
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNumber_aut() {
        return number_aut;
    }

    public void setNumber_aut(String number_aut) {
        this.number_aut = number_aut;
    }

    public String getName_aut() {
        return name_aut;
    }

    public void setName_aut(String name_aut) {
        this.name_aut = name_aut;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPhoto_aut() {
        return photo_aut;
    }

    public void setPhoto_aut(String photo_aut) {
        this.photo_aut = photo_aut;
    }

    public String getAut_class() {
        return aut_class;
    }

    public void setAut_class(String aut_class) {
        this.aut_class = aut_class;
    }

    public String getAut_korobka() {
        return aut_korobka;
    }

    public void setAut_korobka(String aut_korobka) {
        this.aut_korobka = aut_korobka;
    }

    public String getAut_latitude() {
        return aut_latitude;
    }

    public void setAut_latitude(String aut_latitude) {
        this.aut_latitude = aut_latitude;
    }

    public String getAut_longitude() {
        return aut_longitude;
    }

    public void setAut_longitude(String aut_longitude) {
        this.aut_longitude = aut_longitude;
    }
    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
