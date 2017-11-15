package com.github.rasmussaks.tallinntransportapi;

import com.github.rasmussaks.tallinntransportapi.data.Departure;
import com.github.rasmussaks.tallinntransportapi.data.Route;
import com.github.rasmussaks.tallinntransportapi.data.Stop;
import lombok.Cleanup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class TallinnTransportApi {
    private final ApiConfiguration config;
    @Getter
    private final List<Stop> stops = new ArrayList<>();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public TallinnTransportApi() {
        try {
            this.config = ApiConfiguration.builder()
                    .stopsUrl(new URL("https://transport.tallinn.ee/data/stops.txt"))
                    .siriStopDeparturesUrlPattern("https://soiduplaan.tallinn.ee/siri-stop-departures.php?stopid={0}")
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public TallinnTransportApi(ApiConfiguration config) {
        this.config = config;
    }

    public Future<List<Stop>> loadStops() {
        return executor.submit(() -> {
            log.info("Loading stop info from {}", config.getStopsUrl().toString());
            @Cleanup InputStream is = config.getStopsUrl().openStream();
            Scanner sc = new Scanner(is, "UTF-8");
            sc.nextLine(); //Skip header
            Stop last = Stop.builder().build();
            stops.clear();
            while (sc.hasNextLine()) {
                Scanner rsc = new Scanner(sc.nextLine());
                rsc.useDelimiter(";");
                Stop.StopBuilder builder = Stop.builder();
                if (!rsc.hasNext()) continue;
                builder.id(rsc.next());
                builder.siriId(rsc.next());
                builder.lat(rsc.nextInt() / 100000.0);
                builder.lng(rsc.nextInt() / 100000.0);
                builder.neighborData(rsc.hasNext() ? rsc.next() : last.getNeighborData());
                builder.name(rsc.hasNext() ? rsc.next() : last.getName());
                builder.info(rsc.hasNext() ? rsc.next() : last.getInfo());
                builder.street(rsc.hasNext() ? rsc.next() : last.getStreet());
                builder.area(rsc.hasNext() ? rsc.next() : last.getArea());
                builder.city(rsc.hasNext() ? rsc.next() : last.getCity());
                last = builder.build();
                stops.add(last);
            }

            for (Stop stop : stops) {
                for (String neighborId : StringUtils.defaultString(stop.getNeighborData()).split(",")) {
                    for (Stop stop2 : stops) {
                        if (stop2.getId().equals(neighborId)) {
                            stop.getNeighbors().add(stop2);
                            break;
                        }
                    }
                }
            }
            log.info("Loaded {} stops", stops.size());

            return stops;
        });
    }

    public Future<List<Route>> loadRoutes() {
        return executor.submit(() -> {

            return null;
        });
    }

    public Future<List<Departure>> getDeparturesFromStop(Stop stop) {
        return executor.submit(() -> {
            String url = new MessageFormat(config.getSiriStopDeparturesUrlPattern()).format(new String[]{stop.getSiriId()});
            log.info("Loading departure info from {}", url);
            @Cleanup InputStream is = new URL(url).openStream();
            List<Departure> departures = new ArrayList<>();
            Scanner sc = new Scanner(is, "UTF-8");
            sc.nextLine(); // Header
            sc.nextLine(); // Stop information
            while (sc.hasNextLine()) {
                Scanner rsc = new Scanner(sc.nextLine());
                rsc.useDelimiter(",");
                Departure.DepartureBuilder builder = Departure.builder();
                builder.transport(rsc.next());
                builder.routeNum(rsc.next());
                builder.expectedTimeInSeconds(rsc.nextInt());
                builder.scheduledTimeInSeconds(rsc.nextInt());
                builder.routeTo(rsc.next());
                departures.add(builder.build());
            }
            return departures;
        });
    }
}
