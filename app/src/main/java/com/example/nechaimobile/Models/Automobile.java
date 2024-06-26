package com.example.nechaimobile.Models;
public class Automobile {
    private String Number;
    private String Name;
    private String korobka;
    private String latitude;
    private String longitude;
    private String photo_aut;
    private String aut_class;

    public Automobile() {
    }

    public Automobile(String number, String name, String korobka, String latitude, String longitude, String photo_aut,String aut_class) {
        Number = number;
        Name = name;
        this.korobka = korobka;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo_aut = photo_aut;
        this.aut_class=aut_class;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKorobka() {
        return korobka;
    }

    public void setKorobka(String korobka) {
        this.korobka = korobka;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
}
