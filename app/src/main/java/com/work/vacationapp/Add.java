package com.work.vacationapp;

import android.net.Uri;

public class Add {

    private  double Lon;
    private String Name;
    private String Photo1;
    private String id;
    private  double Lat;

    public Add() {
    }

    public Add(String name, String photo1) {
        this.Name = name;
        this.Photo1 = photo1;
    }

    public Add(double lon, String name, String photo1, String id, double lat) {
        this.Lon = lon;
        this.Name = name;
        this.Photo1 = photo1;
        this.id = id;
        this.Lat = lat;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto1() {
        return Photo1;
    }

    public void setPhoto1(String photo1) {
        Photo1 = photo1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
