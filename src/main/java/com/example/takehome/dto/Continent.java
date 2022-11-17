package com.example.takehome.dto;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Continent {
    String code;
    String name;
    Set<Country> countries;
}
