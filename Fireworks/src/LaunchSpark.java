/**
 * Launch sparks will be rendered differently from other sparks, so it is necessary that they
 * are represented by a different class type.
 * @author Alan McLeod
 * @version 1.0
 */
public class LaunchSpark extends Spark {

	private final double RADIUS = 0.0005;	// metre
	
	/**
	 * The LaunchSpark constructor.
	 * @param creationTime The absolute time of creation of the spark.
	 * @param initialXPos The initial position of the spark in the X direction.
	 * @param initialYPos The initial position of the spark in the Y direction
	 * @param initialVX The initial X velocity component of the spark.
	 * @param initialVY The initial Y velocity component of the spark.
	 * @param lifetime The lifetime of the spark in seconds.
	 * @param starColour The colour of the spark.
	 */
	public LaunchSpark(double creationTime, double initialXPos, double initialYPos, double initialVX, 
			double initialVY, double lifetime, String starColour) {
		super(creationTime, initialXPos, initialYPos, initialVX, initialVY, lifetime, starColour);
		this.setStartingRadius(RADIUS);
	}
	
	/**
	 * A simple string representation of the particle.
	 * @return A string containing the type and position in metres.
	 */
	public String toString() {
		return "Launch " + super.toString();
	}

} // end LaunchSpark
