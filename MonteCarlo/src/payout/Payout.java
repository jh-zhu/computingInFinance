package edu.nyu.class3.montecarlo.payout;

import edu.nyu.class3.montecarlo.path.Path;

/**
 *
 */
public interface Payout {

    double payout(Path path);

}
