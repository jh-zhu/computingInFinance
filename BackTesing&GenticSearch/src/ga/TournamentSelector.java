package ga;
/**
 * @author Lee Maclin
 *
 * Implements tournament selection scheme for crossover (similar to a roulette)
 */

public class TournamentSelector {
	
	Chromosome population [];
	
	public TournamentSelector ( Chromosome crs [] ) {
		population = crs;
		// No need to sort as in old selection scheme
	}

	public Chromosome sampleRandom () {
		return population [ ( int ) ( Math.random() * population.length ) ];		
	}
	
	public Chromosome sample () {
		Chromosome cr1 = this.sampleRandom ();
		Chromosome cr2 = this.sampleRandom ();
		return cr1.fitness () > cr2.fitness () ? cr1 : cr2;
	}
	
}
