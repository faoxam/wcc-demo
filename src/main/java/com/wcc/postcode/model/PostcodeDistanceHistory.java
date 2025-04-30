package com.wcc.postcode.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name="postcd_distance_history", indexes = {
        @Index(name="idx_postcode1", columnList="postcode1"),
        @Index(name="idx_postcode2", columnList="postcode2"),
})
public class PostcodeDistanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String postcode1;
    private double latitude1;
    private double longitude1;
    private String postcode2;
    private double latitude2;
    private double longitude2;
    private double kmDistance;
    private LocalDate createDate;

}
