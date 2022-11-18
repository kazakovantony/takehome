package com.example.takehome;

import com.example.takehome.dto.ContinentResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TakehomeApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void Given_Countries_When_ValidRequest_Then_ValidResponse() {

        List<String> coockies = testRestTemplate.getForEntity("/neighbors?countries=US,CA",
                                                              ContinentResponse.class).getHeaders().get("Set-Cookie");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, coockies);
        HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);

        var response = testRestTemplate.exchange("/neighbors?countries=US,CA",
                                                 HttpMethod.GET,
                                                 requestEntity,
                                                 ContinentResponse.class).getBody();

        assertThat(response.getContinent().get(0).getName()).isEqualTo("North America");
    }
}
