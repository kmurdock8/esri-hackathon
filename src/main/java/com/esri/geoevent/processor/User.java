package com.esri.geoevent.processor;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private double lat;
    private double lon;
    List<Integer> watchingList;

    public User(String email, double lat, double lon) {
        this.email = email;
        this.lat = lat;
        this.lon = lon;
        this.watchingList = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLong() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

    public List<Integer> getWatchingList() {
        return watchingList;
    }
    public  void addToWatchingList(int searchID) {
        watchingList.add(searchID);
    }
}
