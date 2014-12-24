package kz.arta.synergy.maps.gwt.client;

import java.io.Serializable;

public class Markers implements Serializable {

    private double lat;
    private double lng;
    private String synergyID;
    private int id;
    private int type;
    private String address;
    private String comment;
    private String number;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getSynergyID() {
        return synergyID;
    }

    public void setSynergyID(String synergyID) {
        this.synergyID = synergyID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}