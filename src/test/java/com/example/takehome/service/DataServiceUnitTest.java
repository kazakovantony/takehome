package com.example.takehome.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {DataService.class, PersistedDataManager.class, ObjectMapper.class})
public class DataServiceUnitTest {

    @Autowired
    private DataService dataService;

    @Test
    public void testGetNeighbors() {
        var continentResponse= dataService.getNeighbors(List.of("CA", "US"));
        assertThat(continentResponse.getContinent().get(0).getName()).isEqualTo("North America");
    }
}
