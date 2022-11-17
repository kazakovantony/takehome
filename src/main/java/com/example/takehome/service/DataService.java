package com.example.takehome.service;

import com.example.takehome.dto.ContinentResponse;
import com.example.takehome.dto.ContinentWrapper;
import com.example.takehome.dto.Country;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService {
    public ContinentResponse getNeighbors(List<String> countryCodes) {
        var continent = PersistedDataManager.COUNTRY_CODE_BY_CONTINENT.get(countryCodes.get(0));
        var neighbors = continent.getCountries().stream()
                                 .map(Country::getCode)
                                 .filter(code -> !countryCodes.contains(code))
                                 .collect(Collectors.toSet());
        return ContinentResponse.builder()
                                .continent(List.of(ContinentWrapper.builder()
                                                                   .name(continent.getName())
                                                                   .countries(new HashSet<>(countryCodes))
                                                                   .otherCountries(neighbors)
                                                                   .build()))
                                .build();
    }
}
