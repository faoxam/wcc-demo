package com.wcc.postcode.service;

import com.wcc.postcode.model.PostcodeDistanceHistory;
import com.wcc.postcode.repository.PostcodeDistanceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostcodeDistanceHistoryService {

    @Autowired
    private PostcodeDistanceHistoryRepository postcodeDistanceHistoryRepository;

    public Optional<PostcodeDistanceHistory> findByPostcode1AndPostcode2(String postcode1, String postcode2) {
        return postcodeDistanceHistoryRepository.findByPostcode1AndPostcode2(postcode1, postcode2);
    }

    public List<PostcodeDistanceHistory> findAll() {
        return postcodeDistanceHistoryRepository.findAll();
    }

    public void save(PostcodeDistanceHistory postcodeDistanceHistory) {
        postcodeDistanceHistoryRepository.save(postcodeDistanceHistory);
    }

}
