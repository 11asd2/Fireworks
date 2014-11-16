/**
 * This class describes the star from a Roman Candle.
 * @author Alan McLeod
 * @version 1.0
 */
public class Star extends Particle {
	
	private final double BURN_RATE = 0.003;	// kg/second
	private final double DENSITY_STAR = 1900;	// kg/m*m*m
	private final double STARTING_MASS = 0.008;	// kg
	
	/**
	 * The Star constructor.
	 * The lifetime of the star is set to (STARTING_MASS / BURN_RATE - 0.5) seconds.
	 * @param creationTime The absolute time of creation of the star.
	 * @param initialXPos The initial position of the star in the X direction.
	 * @param initialYPos The initial position of the star in the Y direction
	 * @param initialVX The initial X velocity component of the star.
	 * @param initialVY The initial Y velocity component of the star.
	 * @param starColour The colour of the star.
	 */
	public Star(double creationTime, double initialXPos, double initialYPos, double initialVX, 
			double initialVY, String starColour) {
		super(creationTime, initialXPos, initialYPos, initialVX, initialVY, starColour);
		setLifetime(STARTING_MASS / BURN_RATE - 0.5);
		setStartingMass(STARTING_MASS);
	}
	
	public int getRenderSize() {
		return 6;
	}
	
	/**
	 * Returns the mass of the star at the given time in seconds.
	 * @param time The time in seconds.
	 * @return The mass of the star in kg.
	 */
	public double getMass(double time) {
		return STARTING_MASS - time * BURN_RATE;
	}
	
	/**
	 * Returns the radius of the star for the given time in seconds.
	 * @param time The time in seconds.
	 * @return The radius of the star in metres.
	 */
	public double getRadius(double time) {
		double volume = getMass(time) / DENSITY_STAR;
		return Math.pow(3 * volume / (4 * Math.PI), 1.0 / 3.0);
	}

	/**
	 * A simple string representation of the particle.
	 * @return A string containing the type, colour and position in metres.
	 */
	public String toString() {
		return "Star" + super.toString();
	}
	
} // end Star class
