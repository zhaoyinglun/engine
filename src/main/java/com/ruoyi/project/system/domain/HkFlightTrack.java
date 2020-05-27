package com.ruoyi.project.system.domain;

import java.util.List;

/**
 * Created by root on 4/26/20.
 */
public class HkFlightTrack {
    private String flightCode;
    private String icao;
    private String srcAirport;
    private String dstAirport;
    private long startTIme;
    private long stopTIme;
    private List<HkTrackPoint> paths;

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public long getStartTIme() {
        return startTIme;
    }

    public void setStartTIme(long startTIme) {
        this.startTIme = startTIme;
    }

    public long getStopTIme() {
        return stopTIme;
    }

    public void setStopTIme(long stopTIme) {
        this.stopTIme = stopTIme;
    }

    public List<HkTrackPoint> getPaths() {
        return paths;
    }

    public void setPaths(List<HkTrackPoint> paths) {
        this.paths = paths;
    }
}
