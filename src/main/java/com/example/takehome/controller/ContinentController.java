package com.example.takehome.controller;

import com.example.takehome.dto.ContinentResponse;
import com.example.takehome.service.DataService;
import com.example.takehome.service.ValidationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/neighbors")
public class ContinentController {

    private final ValidationService validationService;

    private final DataService dataService;

    public ContinentController(ValidationService validationService,
                               DataService dataService) {
        this.validationService = validationService;
        this.dataService = dataService;
    }

    @GetMapping
    public ResponseEntity<ContinentResponse> getNeighbors(@RequestParam("countries") List<String> countries) {
        final ContinentResponse response;
        if(this.validationService.isValid(countries)) {
            response = dataService.getNeighbors(countries);
        } else {
            response = ContinentResponse.builder()
                                        .msg("Countries not found or they are located on different continents")
                                        .build();
        }
        return ResponseEntity.ok(response);
    }
}
