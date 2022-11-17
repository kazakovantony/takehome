package com.example.takehome.clients;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import io.micrometer.core.instrument.util.IOUtils;

public class ApacheHttpClient {

    public static String callGraphQLService(String url, String query) throws URISyntaxException, IOException {
        try(var client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            URI uri = new URIBuilder(request.getURI())
                    .addParameter("query", query)
                    .build();
            request.setURI(uri);
            return IOUtils.toString(client.execute(request).getEntity().getContent());
        }
    }
}
