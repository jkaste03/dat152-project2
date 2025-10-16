/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * 
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OrderNotFoundException(String customMessage) {
		super(customMessage);
	}
	
}
