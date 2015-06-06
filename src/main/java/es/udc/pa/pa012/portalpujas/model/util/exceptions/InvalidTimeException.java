package es.udc.pa.pa012.portalpujas.model.util.exceptions;

@SuppressWarnings("serial")
public class InvalidTimeException extends Exception{

	public  InvalidTimeException(int time) {
		super("Time must be greater than 0");
	}
	
	
}
