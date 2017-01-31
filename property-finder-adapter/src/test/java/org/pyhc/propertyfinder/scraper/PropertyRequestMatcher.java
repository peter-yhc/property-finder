package org.pyhc.propertyfinder.scraper;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.test.web.client.RequestMatcher;

import java.io.IOException;

public class PropertyRequestMatcher implements RequestMatcher {

    @Override
    public void match(ClientHttpRequest clientHttpRequest) throws IOException, AssertionError {
        clientHttpRequest.getURI().getPath().matches("/[property|project]-(.*)-[0-9]+");
    }
}
