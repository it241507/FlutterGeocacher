package at.ac.fhstp.awp_bad.groupxx.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Coordinate {

    private Double lng;
    private Double lat;

    public Coordinate() {
    }
    public Coordinate(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    // Getter and Setter


    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
