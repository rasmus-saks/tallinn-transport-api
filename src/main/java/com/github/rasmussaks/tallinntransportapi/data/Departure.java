package com.github.rasmussaks.tallinntransportapi.data;

import lombok.Builder;
import lombok.Value;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.Instant;

@Value
@Builder
public class Departure {
    private static final String TIMEZONE = "Europe/Tallinn";
    private Stop stop;
    private String transport;
    private String routeNum;
    private int expectedTimeInSeconds;
    private int scheduledTimeInSeconds;
    private String routeTo;

    public Instant getExpectedTime() {
        return Instant.ofEpochMilli(new DateTime(DateTimeZone.forID(TIMEZONE)).withTimeAtStartOfDay().plusSeconds(expectedTimeInSeconds).getMillis());
    }

    public Instant getScheduledTime() {
        return Instant.ofEpochMilli(new DateTime(DateTimeZone.forID(TIMEZONE)).withTimeAtStartOfDay().plusSeconds(scheduledTimeInSeconds).getMillis());
    }
}
