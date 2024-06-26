package com.example.nechaimobile;

public class UserDocument {
    private String userId;
    private String fullName;
    private String passportSeries;
    private String passportNumber;
    private String birthDate;

    public UserDocument() {
        // Пустой конструктор необходим для Firebase
    }

    public UserDocument(String userId, String fullName, String passportSeries, String passportNumber, String birthDate) {
        this.userId = userId;
        this.fullName = fullName;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.birthDate = birthDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
