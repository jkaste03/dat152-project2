package no.hvl.dat152.rest.ws.exceptions;

public class NoOrdersFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NoOrdersFoundException(String customMessage) {
		super(customMessage);
	}
	
}
