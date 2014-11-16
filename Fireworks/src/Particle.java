/**
 * A base class for all particles.
 * @author Alan McLeod
 * @version 2.0
 */
public abstract class Particle extends Firework implements ODESystem {

	private final double DRAG_COEFF = 0.4;		// unitless
	private final int SYSTEM_SIZE = 2;

	private double startingRadius;				// metre
	private double startingMass;				// kg
	private double wind;						// m/sec
	private double creationTime;				// sec
	private double lifetime;					// sec
	private String colour;
	
	/**
	 * The Particle constructor.
	 * @param creationTime The absolute time of creation of the particle.
	 * @param initialXPos The initial position of the particle in the X direction.
	 * @param initialYPos The initial position of the particle in the Y direction
	 * @param initialVX The initial X velocity component of the particle.
	 * @param initialVY The initial Y velocity component of the particle.
	 * @param colour The colour of the particle.
	 */
	public Particle(double creationTime, double initialXPos, double initialYPos, double initialVX, 
			double initialVY, String colour) {
		super(initialXPos, initialYPos, initialVX, initialVY);
		this.creationTime = creationTime;
		this.colour = colour;
	}
	
	/**
	 * Return the render size of the particle.
	 * @return The render size in pixels.
	 */
	public abstract int getRenderSize();
	
	/**
	 * A mutator for the particle lifetime.
	 * @param lifetime The particle lifetime in seconds.
	 */
	public void setLifetime(double lifetime) { 
		this.lifetime = lifetime;
	}
	
	/**
	 * An accessor for the particle lifetime.
	 * @return The particle lifetime in seconds.
	 */
	public double getLifetime() { return lifetime; }
	
	/**
	 * An accessor for the particle creation time.
	 * @return The particle creation time in seconds.
	 */
	public double getCreationTime() { return creationTime; }
	
	/**
	 * An accessor for the particle colour.
	 * @return The colour of the particle.
	 */
	public String getColour() { return colour; }
	
	/**
	 * A mutator for the starting radius of a non-Newtonian particle.
	 * @param radius The radius in metres.
	 */
	public void setStartingRadius(double radius) {
		startingRadius = radius;
	}
	
	/**
	 * A mutator for the starting mass of a non-Newtonian particle.
	 * @param mass The mass in kg.
	 */
	public void setStartingMass(double mass) {
		startingMass = mass;
	}
	
	/**
	 * Returns the particle radius at a given time.
	 * @param time The time in seconds.
	 * @return The radius in metres.
	 */
	public double getRadius(double time) { return startingRadius; }
	
	/**
	 * Returns the mass at a given time.
	 * @param time The time in seconds.
	 * @return The mass in kg.
	 */
	public double getMass(double time) { return startingMass; }
	
	public int getSystemSize() { return SYSTEM_SIZE; }
	
	// Returns the velocity magnitude in m/sec, given the two
	// velocity components.
	private double getVelocityMag(double vx, double vy) {
		return Math.sqrt(vx * vx + vy * vy);
	}
	
	// Calculates the magnitude of the drag force on the star, given time in
	// seconds and the two velocity components in m/sec.
	private double getDragForce(double time, double vx, double vy) {
		double velocityMag = getVelocityMag(vx, vy);
		double radius = getRadius(time);
		double area = Math.PI * radius * radius;
		return Environment.DENSITY_AIR * velocityMag * velocityMag * area * DRAG_COEFF / 2;
	}
	
	// This method returns the value of the fx function, given the 
	// time in seconds and the two velocity components in m/sec.
	// The meaning of fx is described in the assignment statement.
	private double xDE(double time, double vx, double vy) {
		// Use apparent x velocity to calculate drag.
		double vxa = vx - wind;
		double velocityMag = getVelocityMag(vxa, vy);
		double mass = getMass(time);
		double dragForce = getDragForce(time, vxa, vy);
		return -dragForce * vxa / (mass * velocityMag);
	} // end xDE
	
	// This method returns the value of the fy function, given the 
	// time in seconds and the two velocity components in m/sec.
	// The meaning of fy is described in the assignment statement.
	private double yDE(double time, double vx, double vy) {
		// Use apparent x velocity to calculate drag.
		double vxa = vx - wind;
		double velocityMag = getVelocityMag(vxa, vy);
		double mass = getMass(time);
		double dragForce = getDragForce(time, vxa, vy);
		return -Environment.G - dragForce * vy / (mass * velocityMag);
	} // end yDE
	
	public double[] getFunction(double time, double[] values) {
		double[] functionVal = new double[SYSTEM_SIZE];
		double vX = values[0];
		double vY = values[1];
		functionVal[0] = xDE(time, vX, vY);
		functionVal[1] = yDE(time, vX, vY);
		return functionVal;
	}

	/**
	 * A mutator that updates the current position of the particle.
	 * @param time The current time in seconds.
	 * @param deltaTime The time interval in seconds.
	 * @param env An instance of the current Environment object is needed to supply the
	 * wind velocity, which is used to calculate the apparent velocity.
	 */
	public void updatePosition(double time, double deltaTime, Environment env) {
		time = time - getCreationTime();
		wind = env.getWindVelocity();
		double[] newValues = RungeKuttaSolver.getNextPoint(this, time, deltaTime);
		setVelocity(newValues);
		double xVelocity = newValues[0];
		double yVelocity = newValues[1];
		double[] positions = getPosition();
		double xPos = positions[0] + xVelocity * deltaTime;
		double yPos = positions[1] + yVelocity * deltaTime;
		newValues[0] = xPos;
		newValues[1] = yPos;
		setPosition(newValues);
	}
	
	/**
	 * A string representation of the particle.
	 * @return A string containing the colour and position of the particle.
	 */
	public String toString() {
		return ", " + colour + ", at" + super.toString();
	} // end toString
	
} // end Particle
