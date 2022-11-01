package audience.merchandising.controller;

import java.util.List;

public class Publisher {
    private String name;
    private String total = "100,000";
    private String overlap;
    private String time;
    private String session;
    private String sessionTime="5";
    private String region;
    private String regionList;
    private List<String> countries;
    private String period="5";
    private double overlapPercent;
    private String accuracy;
    private String timeRadio;

    public String getTimeRadio() {
        return timeRadio;
    }

    public void setTimeRadio(String timeRadio) {
        this.timeRadio = timeRadio;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public double getOverlapPercent() {
        return overlapPercent;
    }

    public void setOverlapPercent(double overlapPercent) {
        this.overlapPercent = overlapPercent;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public String getRegionList() {
        return regionList;
    }

    public void setRegionList(String regionList) {
        this.regionList = regionList;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverlap() {
        return overlap;
    }

    public void setOverlap(String overlap) {
        this.overlap = overlap;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
