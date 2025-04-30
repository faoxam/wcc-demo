package com.wcc.postcode.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvImportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void importCsvToTable(String csvFilePath) throws IOException{
        Path path = Paths.get(csvFilePath);
        if (!Files.exists(path)) {
            throw new IOException("CSV files not found");
        }

        String sql = String.format("LOAD DATA LOCAL INFILE '%s' INTO TABLE postcode FIELDS TERMINATED BY ',' IGNORE 1 LINES", csvFilePath);
        jdbcTemplate.execute(sql);
    }
}
