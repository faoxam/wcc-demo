package com.wcc.postcode.controller;


import com.wcc.postcode.model.PostcodeGeo;
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
import java.util.*;

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
        Map<String, Object> errorResponse = new HashMap<>();
        PostcodeGeo postcodeGeo1;
        PostcodeGeo postcodeGeo2;
        double distanceKm;

        // Query from history table first
        PostcodeDistanceHistory postcodeDistanceHistory;
        try {
            postcodeDistanceHistory = postcodeDistanceHistoryService.findByPostcode1AndPostcode2(postCode1, postCode2).orElseThrow();
        } catch (Exception e) {

            // If not found, then calculate the distance
            try {
                postcodeGeo1 = postcodeService.getGeolocation(postCode1).orElseThrow();
                postcodeGeo2 = postcodeService.getGeolocation(postCode2).orElseThrow();

                // Case where both latitude and longitude is 0, mean not yet assigned the value
                if ((postcodeGeo1.getLatitude() == 0 && postcodeGeo1.getLongitude() == 0) ||
                        (postcodeGeo2.getLatitude() == 0 && postcodeGeo2.getLongitude() == 0)) {
                    errorResponse.put("error-message", "One or both postal is valid but yet not defined the coordinate");
                    errorResponse.put("error-code", HttpStatus.NOT_FOUND);
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }
                distanceKm = postcodeService.calculateDistance(
                        postcodeGeo1.getLatitude(),
                        postcodeGeo1.getLongitude(),
                        postcodeGeo2.getLatitude(),
                        postcodeGeo2.getLongitude());

                // Only update the first time successful calculation into history table
                postcodeDistanceHistory = new PostcodeDistanceHistory();
                postcodeDistanceHistory.setPostcode1(postcodeGeo1.getPostcode());
                postcodeDistanceHistory.setLatitude1(postcodeGeo1.getLatitude());
                postcodeDistanceHistory.setLongitude1(postcodeGeo1.getLongitude());
                postcodeDistanceHistory.setPostcode2(postcodeGeo2.getPostcode());
                postcodeDistanceHistory.setLatitude2(postcodeGeo2.getLatitude());
                postcodeDistanceHistory.setLongitude2(postcodeGeo2.getLongitude());
                postcodeDistanceHistory.setKmDistance(distanceKm);
                postcodeDistanceHistory.setCreateDate(LocalDate.now());
                postcodeDistanceHistoryService.save(postcodeDistanceHistory);

            } catch (NoSuchElementException ex) {
                errorResponse.put("error-message", "One or both postal codes could not be found.");
                errorResponse.put("error-code", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        }

        Map<String, Object> response = formatingResponse(postcodeDistanceHistory);
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
        List<Map<String, Object>> result = new ArrayList<>();
        for (PostcodeDistanceHistory postcodeDistanceHistory: postcodeDistanceHistoryList) {
            result.add(formatingResponse(postcodeDistanceHistory));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Map<String, Object> formatingResponse(PostcodeDistanceHistory postcodeDistanceHistory) {
        Map<String, Object> response = new HashMap<>();
        PostcodeGeo from = new PostcodeGeo();
        from.setPostcode(postcodeDistanceHistory.getPostcode1());
        from.setLatitude(postcodeDistanceHistory.getLatitude1());
        from.setLongitude(postcodeDistanceHistory.getLongitude1());

        PostcodeGeo to = new PostcodeGeo();
        to.setPostcode(postcodeDistanceHistory.getPostcode2());
        to.setLatitude(postcodeDistanceHistory.getLatitude2());
        to.setLongitude(postcodeDistanceHistory.getLongitude2());

        response.put("From", from);
        response.put("To", to);
        response.put("distanceKm", String.format("%.2f", postcodeDistanceHistory.getKmDistance()) + " km");
        return response;
    }
}