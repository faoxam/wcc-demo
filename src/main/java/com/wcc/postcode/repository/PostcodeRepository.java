package com.wcc.postcode.repository;

import com.wcc.postcode.model.Postcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostcodeRepository extends JpaRepository<Postcode, String> {
    Optional<Postcode> findByPostcode(String postcode);
}
