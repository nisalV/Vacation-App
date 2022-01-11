package com.work.vacationapp;

public class Position {
    double lan;
    double lon;

    public Position() {
    }

    public Position(double lan, double lon) {
        this.lan = lan;
        this.lon = lon;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
