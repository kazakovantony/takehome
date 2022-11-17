package com.example.takehome.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {
    public boolean isValid(List<String> countries) {
        boolean result = false;
        var continent = PersistedDataManager.COUNTRY_CODE_BY_CONTINENT.get(countries.get(0));

        for (var countryCode : countries) {
            result = true;
            var currentContinent = PersistedDataManager.COUNTRY_CODE_BY_CONTINENT.get(countryCode);
            if (currentContinent == null || !currentContinent.equals(continent)) {
                return false;
            }
        }
        return result;
    }
}
