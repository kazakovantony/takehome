package com.example.takehome.service;

import com.example.takehome.clients.ApacheHttpClient;
import com.example.takehome.dto.Continent;
import com.example.takehome.dto.Country;
import com.example.takehome.dto.DataResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

@Service
public class PersistedDataManager {

    public static final Map<String, Continent> COUNTRY_CODE_BY_CONTINENT = new ConcurrentHashMap<>();

    public static final Map<String, Set<Country>> CONTINENT_CODE_BY_COUNTRIES = new ConcurrentHashMap<>();

    private static final String COUNTRIES_DATA_PROVIDER = "https://countries.trevorblades.com/graphql";

    private static final String ALL_COUNTRIES_QUERY = "{continents(filter: {code: {nin: [\"not_existed\"]}}){code,name,countries{code,name}}}";

    private final ObjectMapper objectMapper;

    @Value("${spring.application.storage}")
    private String persistedDataPath;

    public PersistedDataManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws URISyntaxException, IOException {
        var file = ResourceUtils.getFile(persistedDataPath);
        DataResponse dataResponse = loadData(file);
        fillCache(dataResponse);
    }

    private DataResponse loadData(File file) throws URISyntaxException, IOException {
        final String data;
        if(!file.exists()) {
            data = ApacheHttpClient.callGraphQLService(COUNTRIES_DATA_PROVIDER, ALL_COUNTRIES_QUERY);
            Files.deleteIfExists(file.toPath());
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(data);
            }
        } else {
            data = Files.readString(file.toPath());
        }
        return objectMapper.readValue(data, DataResponse.class);
    }

    private static void fillCache(DataResponse dataResponse) {
        for (var continent: dataResponse.getData().getContinents()) {
            CONTINENT_CODE_BY_COUNTRIES.put(continent.getCode(), continent.getCountries());
            continent.getCountries().forEach(country -> COUNTRY_CODE_BY_CONTINENT.put(country.getCode(), continent));
        }
    }
}
