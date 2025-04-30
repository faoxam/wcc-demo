package com.wcc.postcode.service;

import com.wcc.postcode.model.PostcodeGeo;
import com.wcc.postcode.repository.PostcodeRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@Service
public class DataImportService {

    private static final Logger logger = LoggerFactory.getLogger(DataImportService.class);

    @Autowired
    private  PostcodeRepository postcodeRepository;

    @Value("classpath:data/ukpostcodes.csv") // Assuming your CSV is in src/main/resources/data/users.csv
    private Resource csvFile;

    // @PostConstruct
    @Transactional
    public void importDataFromCsv() {
        logger.info("Starting CSV data import...");
        try (Reader reader = new InputStreamReader(csvFile.getInputStream());
             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.DEFAULT.withHeader(
                             "id",
                             "postcode",
                             "latitude",
                             "longitude").withSkipHeaderRecord())) {

            for (CSVRecord record : csvParser) {
                try {
                    long id = Long.parseLong(record.get("id"));
                    String postcode = record.get("postcode");
                    double latitude = Double.parseDouble(record.get("latitude"));
                    double longitude = Double.parseDouble(record.get("longitude"));

                    // You might want to add validation or error handling here
                    PostcodeGeo model = new PostcodeGeo();
                    model.setId(id);
                    model.setPostcode(postcode);
                    model.setLatitude(latitude);
                    model.setLongitude(longitude);
                    postcodeRepository.save(model);
                } catch (IllegalArgumentException e) {
                    logger.warn("Skipping record due to missing or invalid data: {}", record.toMap(), e);
                }
            }
            logger.info("CSV data import completed successfully.");

        } catch (IOException e) {
            logger.error("Error during CSV file reading or processing: {}", e.getMessage(), e);
        }
    }
}