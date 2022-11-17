package com.example.takehome;

import com.example.takehome.dto.ContinentResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TakehomeApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void Given_Countries_When_ValidRequest_Then_ValidResponse() {

        var response = testRestTemplate.getForEntity("/neighbors?countries=US,CA",
                                                     ContinentResponse.class).getBody();
        assertThat(response.getContinent().get(0).getName()).isEqualTo("North America");
    }
}
