package com.wcc.postcode.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="postcd_geo", indexes = {
        @Index(name="idx_postcode", columnList="postcode", unique=true)
})
public class PostcodeGeo {

    @Id
    private long id;
    @Column(unique = true)
    private String postcode;
    private double latitude;
    private double longitude;

}
