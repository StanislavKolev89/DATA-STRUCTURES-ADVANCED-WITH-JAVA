package core;

import models.Route;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MoovItImpl implements MoovIt {

    private final Map<String, Route> routeMap;
    private final Set<Route> routeSet;


    public MoovItImpl() {
        this.routeMap = new LinkedHashMap<>();
        this.routeSet = new HashSet<>();
    }

    @Override
    public void addRoute(Route route) {
        if (this.routeSet.contains(route)) {
            throw new IllegalArgumentException();
        }
        this.routeMap.put(route.getId(), route);
        this.routeSet.add(route);

    }

    @Override
    public void removeRoute(String routeId) {
        Route remove = this.routeMap.remove(routeId);

        if (remove == null) {
            throw new IllegalArgumentException();
        }
        this.routeSet.remove(remove);

    }

    @Override
    public boolean contains(Route route) {
        return this.routeSet.contains(route);
    }

    @Override
    public int size() {
        return this.routeSet.size();
    }

    @Override
    public Route getRoute(String routeId) {
        Route route = this.routeMap.get(routeId);
        if (route == null) {
            throw new IllegalArgumentException();
        }
        return route;
    }

    @Override
    public void chooseRoute(String routeId) {
        Route route = this.getRoute(routeId);

        route.setPopularity(route.getPopularity() + 1);

    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {

        return this.routeSet.stream()
                .filter(r -> {
                    int indexOfStart = r.getLocationPoints().indexOf(startPoint);
                    int indexOfEnd = r.getLocationPoints().indexOf(endPoint);
                    return indexOfStart < indexOfEnd && indexOfStart > -1;
                })
                .sorted((f, s) -> {
                            if (f.getIsFavorite() && !s.getIsFavorite()) {
                                return -1;
                            } else if (!f.getIsFavorite() && s.getIsFavorite()) {
                                return 1;
                            }

                            int fDistance = f.getLocationPoints().indexOf(endPoint) - f.getLocationPoints().indexOf(startPoint);
                            int sDistance = s.getLocationPoints().indexOf(endPoint) - s.getLocationPoints().indexOf(startPoint);

                            if (fDistance != sDistance) {
                                return fDistance - sDistance;
                            }
                            return s.getPopularity() - f.getPopularity();
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        return this.routeSet.stream().filter(r -> r.getIsFavorite() && r.getLocationPoints().contains(destinationPoint) && r.getLocationPoints().indexOf(destinationPoint) != 0)
                .sorted((f, s) -> {
                    if (!f.getDistance().equals(s.getDistance())) {
                        return Double.compare(f.getDistance(), s.getDistance());
                    }
                    return s.getPopularity() - f.getPopularity();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints() {
        return this.routeSet.stream().sorted((f, s) -> {
            if (!f.getPopularity().equals(s.getPopularity())) {
                return s.getPopularity() - f.getPopularity();
            }
            if (!f.getDistance().equals(s.getDistance())) {
                return Double.compare(f.getDistance(), s.getDistance());
            }
            return f.getLocationPoints().size() - s.getLocationPoints().size();
        }).limit(5).collect(Collectors.toList());
    }
}
