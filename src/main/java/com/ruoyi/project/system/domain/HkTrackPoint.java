package com.ruoyi.project.system.domain;

/**
 * Created by root on 4/26/20.
 */
public class HkTrackPoint {
    private long time;
    private float longitude;
    private float latitude;
    private float baltitude;
    private float track;
    private float speed;
    private boolean on_ground;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public float getBaltitude() {
        return baltitude;
    }

    public void setBaltitude(float baltitude) {
        this.baltitude = baltitude;
    }

    public float getTrack() {
        return track;
    }

    public void setTrack(float track) {
        this.track = track;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isOn_ground() {
        return on_ground;
    }

    public void setOn_ground(boolean on_ground) {
        this.on_ground = on_ground;
    }
}
