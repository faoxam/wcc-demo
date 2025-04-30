package com.wcc.postcode.repository;

import com.wcc.postcode.model.PostcodeDistanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostcodeDistanceHistoryRepository extends JpaRepository<PostcodeDistanceHistory, String> {
    Optional<PostcodeDistanceHistory> findByPostcode1AndPostcode2(String postcode1, String postcode2);
}
