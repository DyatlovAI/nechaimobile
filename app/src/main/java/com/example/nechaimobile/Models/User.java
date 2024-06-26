package com.example.nechaimobile.Models;

public class User {
    private String userId;
    private String role;
    private String phone;
    private String email;
    private String password;
    private String address;
    private String timestamp;
    private String notes;
    private String iDs;

    public User() {
    }

    public User(String userId, String role, String phone, String email, String password, String address, String timestamp, String notes, String iDs) {
        this.userId = userId;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.timestamp = timestamp;
        this.notes = notes;
        this.iDs = iDs;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getiDs() {
        return iDs;
    }

    public void setiDs(String iDs) {
        this.iDs = iDs;
    }
}
