package edu.nyu.class3.montecarlo.random;

/**
 *
 */
public class ShiftDecorator implements RandomNumberGenerator {

    private RandomNumberGenerator generator;
    private double c;

    public ShiftDecorator(RandomNumberGenerator generator, double c) {
        this.c = c;
        this.generator = generator;
    }

    public double nextRandom() {
        return c + generator.nextRandom();
    }



}
