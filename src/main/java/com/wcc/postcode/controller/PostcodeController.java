package com.wcc.postcode.controller;


import com.wcc.postcode.model.Postcode;
import com.wcc.postcode.model.PostcodeDistanceHistory;
import com.wcc.postcode.service.PostcodeDistanceHistoryService;
import com.wcc.postcode.service.PostcodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = PostcodeController.BASE_URL)
public class PostcodeController {

    public static final String BASE_URL = "/";
    @Autowired
    private PostcodeService postcodeService;

    @Autowired
    private PostcodeDistanceHistoryService postcodeDistanceHistoryService;

    @GetMapping("distance/{postCode1}/{postCode2}")
    @Operation(summary = "Calculate distance between 2 postcode", description = "Access Roles: [User]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<?> getDistance(@PathVariable String postCode1, @PathVariable String postCode2) {
        Postcode model1;
        Postcode model2;
        double distanceKm;

        // Query from history table first
        try {
            PostcodeDistanceHistory postcodeDistanceHistory = postcodeDistanceHistoryService.findByPostcode1AndPostcode2(postCode1, postCode2).orElseThrow();
            distanceKm = postcodeDistanceHistory.getKmDistance();
        } catch (Exception e) {

            // If not found, then calculate the distance
            try {
                model1 = postcodeService.getGeolocation(postCode1).orElseThrow();
                model2 = postcodeService.getGeolocation(postCode2).orElseThrow();
                distanceKm = postcodeService.calculateDistance(
                        model1.getLatitude(),
                        model1.getLongitude(),
                        model2.getLatitude(),
                        model2.getLongitude());

                // Only update the first time successful calculation into history table
                PostcodeDistanceHistory postcodeDistanceHistory = new PostcodeDistanceHistory();
                postcodeDistanceHistory.setPostcode1(postCode1);
                postcodeDistanceHistory
                        .setPostcode2(postCode2);
                postcodeDistanceHistory.setKmDistance(distanceKm);
                postcodeDistanceHistory.setCreateBy("wcc_user");
                postcodeDistanceHistory.setCreateDate(LocalDate.now());
                postcodeDistanceHistoryService.save(postcodeDistanceHistory);

            } catch (NoSuchElementException ex) {
                return new ResponseEntity<>("One or both postal codes could not be found.", HttpStatus.NOT_FOUND);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("postalCode1", postCode1);
        response.put("postalCode2", postCode2);
        response.put("distanceKm", String.format("%.2f", distanceKm));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/history")
    @Operation(summary = "Get all histories", description = "Access Roles: [User]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<?> getHistory() {
        List<PostcodeDistanceHistory> postcodeDistanceHistoryList = postcodeDistanceHistoryService.findAll();
        return new ResponseEntity<>(postcodeDistanceHistoryList, HttpStatus.OK);
    }
}