package com.wcc.postcode.model;

import jakarta.persistence.*;

@Entity
@Table(name="postcd_geo", indexes = {
        @Index(name="idx_postcode", columnList="postcode", unique=true)
})
public class Postcode {

    @Id
    private long id;
    @Column(unique = true)
    private String postcode;
    private double latitude;
    private double longitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
