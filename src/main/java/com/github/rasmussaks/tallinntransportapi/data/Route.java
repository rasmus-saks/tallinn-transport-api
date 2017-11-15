package com.github.rasmussaks.tallinntransportapi.data;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Route {
    private String routeNum;
    private String authority;
    private String city;
    private String transport;
    private String operator;
    private String validityPeriods;
    private String specialDates;
    private String routeTag;
    private String routeType;
    private String commercial;
    private String routeName;
    private String weekdays;
    private String streets;
    private String routeStops;
    private String routeStopsPlatforms;
    private String timetable;
}
