package com.work.vacationapp;

public class Advertisement {

    private String id;
    private String Address;
    private String Description;
    private String Email;
    private double Lat;
    private double Lon;
    private String Name;
    private String Phone;
    private String Photo1;
    private String Photo2;
    private String Photo3;
    private String Photo4;

    public Advertisement() {
    }

    public Advertisement(String name, String photo1) {
        Name = name;
        Photo1 = photo1;
    }

    public Advertisement(String id, String address, String description, String email, double lat, double lon, String name, String phone, String photo1, String photo2, String photo3, String photo4) {
        id = id;
        Address = address;
        Description = description;
        Email = email;
        Lat = lat;
        Lon = lon;
        Name = name;
        Phone = phone;
        Photo1 = photo1;
        Photo2 = photo2;
        Photo3 = photo3;
        Photo4 = photo4;
    }
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public String getName() { return Name; }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPhoto1() {
        return Photo1;
    }

    public void setPhoto1(String photo1) {
        Photo1 = photo1;
    }

    public String getPhoto2() {
        return Photo2;
    }

    public void setPhoto2(String photo2) {
        Photo2 = photo2;
    }

    public String getPhoto3() {
        return Photo3;
    }

    public void setPhoto3(String photo3) {
        Photo3 = photo3;
    }

    public String getPhoto4() {
        return Photo4;
    }

    public void setPhoto4(String photo4) {
        Photo4 = photo4;
    }
}
