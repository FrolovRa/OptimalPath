package com.bushemi;

import java.util.*;

public class AStarF implements AStarInterface {

    private MapCell start;
    private MapCell finish;
    private Map2D map;
    private List<MapCell> path;

    private double getLengthToFinish(MapCell point) {
        return Math.sqrt(Math.pow(finish.getX() - point.getX(), 2) + Math.pow(finish.getY() - point.getY(), 2));
    }

    private MapCell decideDirection(MapCell currentLocation, MapCell previousLocation) {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        Map<MapCell, Double> decisions = new HashMap<>();

        for (int i = x - 1; i <= x + 1; i = i + 2) {
            try {
                MapCell cell = map.getMapCell(i, y);
                Double length = this.getLengthToFinish(cell);

                if (cell.isPassable() && !path.contains(cell)) decisions.put(cell, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = y - 1; i <= y + 1; i = i + 2) {
            try {
                MapCell cell = map.getMapCell(x, i);
                Double length = this.getLengthToFinish(cell);

                if (cell.isPassable() && !path.contains(cell)) decisions.put(cell, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return decisions.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(previousLocation);
    }

    @Override
    public List<MapCell> getPath(Map2D map, MapCell start, MapCell finish) {
        this.finish = finish;
        this.start = start;
        this.map = map;
        path = new LinkedList<>();
        path.add(start);

        MapCell currentLocation = start;
        MapCell previousLocation = start;
        while (!currentLocation.equals(finish)) {
            currentLocation = this.decideDirection(currentLocation, previousLocation);
            path.add(currentLocation);
            previousLocation = currentLocation;
            if (path.size() == map.getMapSize()) return path;
        }

        return path;
    }
}