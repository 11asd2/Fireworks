import java.util.ArrayList;
/**
 * Emitter for delay charge sparks.
 * @author Alan McLeod
 * @version 1.1
 */
public class DelaySparkEmitter extends Emitter {

	private final double LIFETIME = 0.6;	// seconds
	private final int NUM_LAUNCHED = 5;		// how many to launch
	
	/**
	 * The DelaySparkEmitter constructor.
	 * @param initialXPos The initial X position of the emitter.
	 * @param initialYPos The initial Y position of the emitter.
	 * @param initialXV The initial X velocity component of the emitter.
	 * @param initialYV The initial Y velocity component of the emitter.
	 * @param exitVelocity The launch velocity of the sparks from the emitter.
	 * @param firingAngle The launch angle of the emitter, from the vertical in degrees.
	 * @param variation The random variation range for the launch angle in degrees.
	 * @throws EmitterException If the two angles are not legal.
	 */
	public DelaySparkEmitter(double initialXPos, double initialYPos, double initialXV, double initialYV,
			double exitVelocity, double firingAngle, double variation) throws EmitterException {
		super(initialXPos, initialYPos, initialXV, initialYV, exitVelocity, firingAngle, variation);
	}
	
	/**
	 * Launches (returns) Spark objects in an ArrayList at the supplied time.
	 * @param time The absolute launch time in seconds.
	 * @return An ArrayList containing Sparks.
	 */
	public ArrayList<Spark> launch(double time) {
		double angle, vXInitial, vYInitial;
		double[] position = getPosition();
		double[] velocity = getVelocity();
		ArrayList<Spark> sparks = new ArrayList<>(NUM_LAUNCHED);
		for (int i = 0; i < NUM_LAUNCHED; i++) {
			angle = getRandomLaunchAngle();
			vXInitial = velocity[0] + getExitVelocity() * Math.sin(angle);
			vYInitial = velocity[1] + getExitVelocity() * Math.cos(angle);
			sparks.add(new Spark(time, position[0], position[1], vXInitial, vYInitial, LIFETIME, "orange"));
		}
		return sparks;
	} // end launch

} // end DelaySparkEmitter
