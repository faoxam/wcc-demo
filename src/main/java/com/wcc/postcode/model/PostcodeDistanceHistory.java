package com.wcc.postcode.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="postcd_distance_history", indexes = {
        @Index(name="idx_postcode1", columnList="postcode1"),
        @Index(name="idx_postcode2", columnList="postcode2"),
})
public class PostcodeDistanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String postcode1;
    private String postcode2;
    private double kmDistance;
    private String createBy;
    private LocalDate createDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostcode1() {
        return postcode1;
    }

    public void setPostcode1(String postcode1) {
        this.postcode1 = postcode1;
    }

    public String getPostcode2() {
        return postcode2;
    }

    public void setPostcode2(String postcode2) {
        this.postcode2 = postcode2;
    }

    public double getKmDistance() {
        return kmDistance;
    }

    public void setKmDistance(double kmDistance) {
        this.kmDistance = kmDistance;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }
}
