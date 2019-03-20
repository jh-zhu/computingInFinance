package edu.nyu.class3.montecarlo.path;

import java.util.*;


/**
 *
 */
public class Path {

    private SortedSet<DataPoint> points;

    public Path() {
        points = new TreeSet<>(Comparator.comparing(p -> p.date()));
    }

    public void addDataPoint(DataPoint dataPoint) {
        points.add(dataPoint);
    }

    public SortedSet<DataPoint> getData() {
        return Collections.unmodifiableSortedSet(points);
    }


}
