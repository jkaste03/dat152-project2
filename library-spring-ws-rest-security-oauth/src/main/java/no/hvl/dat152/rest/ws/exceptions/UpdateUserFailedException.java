/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

/**
 * 
 */
public class UpdateUserFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public UpdateUserFailedException(String customMessage) {
        super(customMessage);
    }

    public UpdateUserFailedException(String customMessage, Throwable cause) {
        super(customMessage, cause);
    }

}
