import java.util.ArrayList;

/**
 * Emitter for Star sparks.
 * @author Alan McLeod
 * @version 1.1
 */
public class StarSparkEmitter extends Emitter {
		
	private final double LIFETIME = 0.1;		// seconds
	private final int NUM_LAUNCHED = 20;		// how many to launch
	private String colour;
	
	/**
	 * The StarSparkEmitter constructor.
	 * @param initialXPos The initial X position of the emitter.
	 * @param initialYPos The initial Y position of the emitter.
	 * @param initialXV The initial X velocity component of the emitter.
	 * @param initialYV The initial Y velocity component of the emitter.
	 * @param exitVelocity The launch velocity of the sparks from the emitter.
	 * @param firingAngle The launch angle of the emitter, from the vertical in degrees.
	 * @param variation The random variation range for the launch angle in degrees.
	 * @throws EmitterException If the two angles are not legal.
	 */
	public StarSparkEmitter(double initialXPos, double initialYPos, double initialXV, double initialYV,
			double exitVelocity, double firingAngle, double variation) throws EmitterException {
		super(initialXPos, initialYPos, initialXV, initialYV, exitVelocity, firingAngle, variation);
	}
	/**
	 * A mutator for the colour of the StarSpark to be launched.
	 * @param colour The desired StarSpark colour.
	 */
	public void setColour (String colour) {
		this.colour = colour;
	}
		
	/**
	 * Launches (returns) StarSpark objects at the supplied time.
	 * @param time The absolute launch time in seconds.
	 * @return An ArrayList of StarSpark objects.
	 */
	public ArrayList<Spark> launch(double time) {
		double angle, vXInitial, vYInitial;
		double[] position = getPosition();
		double[] velocity = getVelocity();
		ArrayList<Spark> sparks = new ArrayList<>(NUM_LAUNCHED);
		double randomTime;
		for (int i = 0; i < NUM_LAUNCHED; i++) {
			angle = getRandomLaunchAngle();
			vXInitial = velocity[0] + getExitVelocity() * Math.sin(angle);
			vYInitial = velocity[1] + getExitVelocity() * Math.cos(angle);
			// Add some slight variation to time to scatter the sparks a bit more
			randomTime = time + 1.5 * (Math.random() - 0.5);
			sparks.add(new Spark(randomTime, position[0], position[1], vXInitial, vYInitial, LIFETIME, colour));
		}
		return sparks;
	}

} // end StarSparkEmitter
