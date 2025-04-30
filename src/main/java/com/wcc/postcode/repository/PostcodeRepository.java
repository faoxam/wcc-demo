package com.wcc.postcode.repository;

import com.wcc.postcode.model.PostcodeGeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostcodeRepository extends JpaRepository<PostcodeGeo, String> {
    Optional<PostcodeGeo> findByPostcode(String postcode);
}
