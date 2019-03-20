package edu.nyu.class3.montecarlo.path;

import java.time.LocalDate;

/**
 *
 */
public class DataPoint {

    private LocalDate date;
    private double price;

    public LocalDate date() {
        return date;
    }

    public DataPoint date(LocalDate date) {
        this.date = date;
        return this;
    }

    public double price() {
        return price;
    }

    public DataPoint price(double price) {
        this.price = price;
        return this;
    }
}
