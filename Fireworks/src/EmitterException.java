/**
 * Thrown by the Emitter object if an illegal angle is supplied.
 * @author Alan McLeod
 * @version 1.0
 */
public class EmitterException extends Exception {

	/**
	 * Accepts a specific message about the problem.
	 * @param message
	 */
	public EmitterException(String message) {
		super(message);
	}
	
} // end EmitterException class
