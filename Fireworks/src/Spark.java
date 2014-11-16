/**
 * A spark.
 * @author Alan McLeod
 * @version 1.0
 */
public class Spark extends Particle {
	
	private final double RADIUS = 0.0015;	// metre
	private final double MASS = 2E-6;		// kg
	
	/**
	 * The Spark constructor.
	 * The lifetime of the spark is set to 0.6 seconds and the render size to 2 pixels.
	 * @param creationTime The absolute time of creation of the spark.
	 * @param initialXPos The initial position of the spark in the X direction.
	 * @param initialYPos The initial position of the spark in the Y direction
	 * @param initialVX The initial X velocity component of the spark.
	 * @param initialVY The initial Y velocity component of the spark.
	 * @param lifetime The lifetime of the spark in seconds.
	 * @param starColour The colour of the spark.
	 */
	public Spark(double creationTime, double initialXPos, double initialYPos, double initialVX, 
			double initialVY, double lifetime, String starColour) {
		super(creationTime, initialXPos, initialYPos, initialVX, initialVY, starColour);
		setLifetime(lifetime);
		setStartingMass(MASS);
		setStartingRadius(RADIUS);
	} // end constructor

	public int getRenderSize() {
		return 2;
	}
	
	/**
	 * A simple string representation of the particle.
	 * @return A string containing the type and position in metres.
	 */
	public String toString() {
		return "Spark" + super.toString();
	}

} // end Spark
