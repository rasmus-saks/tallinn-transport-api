package com.github.rasmussaks.tallinntransportapi;

import lombok.Builder;
import lombok.Value;

import java.net.URL;

@Value
@Builder
public class ApiConfiguration {
    private URL stopsUrl;
    private URL routesUrl;
    private String siriStopDeparturesUrlPattern;
}
