package com.ruoyi.project.system.domain;

/**
 * Created by root on 4/26/20.
 */
public class AdsbVector {
    private String icao;
    private String timestamp;
    private String flight;
    private float longitude;
    private float latitude;
    private float speed;
    private float aspeed;
    private float vspeed;
    private float baltitude;
    private float galtitude;
    private float track;
    private String squawk;
    private int status;
    private int src;

    public String getIcao() {
        return icao;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAspeed() {
        return aspeed;
    }

    public void setAspeed(float aspeed) {
        this.aspeed = aspeed;
    }

    public float getVspeed() {
        return vspeed;
    }

    public void setVspeed(float vspeed) {
        this.vspeed = vspeed;
    }

    public float getBaltitude() {
        return baltitude;
    }

    public void setBaltitude(float baltitude) {
        this.baltitude = baltitude;
    }

    public float getGaltitude() {
        return galtitude;
    }

    public void setGaltitude(float galtitude) {
        this.galtitude = galtitude;
    }

    public float getTrack() {
        return track;
    }

    public void setTrack(float track) {
        this.track = track;
    }

    public String getSquawk() {
        return squawk;
    }

    public void setSquawk(String squawk) {
        this.squawk = squawk;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }
}
