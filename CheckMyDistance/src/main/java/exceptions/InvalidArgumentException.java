package exceptions;

/**
 * Exception in case an argument has a invalid value
 * 
 * @author jvdur_000
 *
 */
public class InvalidArgumentException extends Exception {
		
	private static final long serialVersionUID = 1L;

	public InvalidArgumentException(String msg) {
		super(msg);
	}
	
}
