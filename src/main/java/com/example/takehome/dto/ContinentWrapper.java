package com.example.takehome.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ContinentWrapper {
    Set<String> countries;
    String      name;
    Set<String> otherCountries;
}

