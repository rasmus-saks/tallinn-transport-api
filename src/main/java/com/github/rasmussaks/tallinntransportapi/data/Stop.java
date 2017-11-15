package com.github.rasmussaks.tallinntransportapi.data;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class Stop {
    private String id;
    private String siriId;
    private double lat;
    private double lng;
    private String name;
    private String info;
    private String street;
    private String area;
    private String city;
    private List<Stop> neighbors = new ArrayList<>();
    private String neighborData;
}
