package com.github.rasmussaks.tallinntransportapi

class TallinnTransportApiSpec extends BaseSpecification {
    TallinnTransportApi api = new TallinnTransportApi(ApiConfiguration
            .builder()
            .stopsUrl(TallinnTransportApiSpec.getResource("/stops_basic.txt"))
            .siriStopDeparturesUrlPattern(TallinnTransportApiSpec.getResource("/departures_1135.txt").toString().replace('1135.txt$', "{0}.txt"))
            .build())

    def "loads stops"() {
        when:
            def stops = api.loadStops().get()
        then:
            stops.size() == 1
            stops[0].id == "08904-1"
            stops[0].siriId == "1135"
            stops[0].lat == (double) 59.42749
            stops[0].lng == (double) 24.72192
            stops[0].neighborData == "08906-1"
            stops[0].name == "Taksopark"
            stops[0].info == "Extra information"
            stops[0].street == "The street"
            stops[0].area == "The area"
            stops[0].city == "The city"

    }

    def "gets next departures"() {
        when:
            api.loadStops().get()
            def departures = api.getDeparturesFromStop(api.getStops()[0]).get()
        then:
            departures.size() == 2
            departures[0].transport == "bus"
            departures[0].routeNum == "42"
            departures[0].expectedTimeInSeconds == 10
            departures[0].scheduledTimeInSeconds == 30
            departures[0].routeTo == "Kaubamaja"
            println(departures[0].getExpectedTime())
            println(departures[0].getScheduledTime())
    }
}
