package es.udc.pa.pa012.portalpujas.model.util.exceptions;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class InvalidPriceException extends Exception{

	
	public  InvalidPriceException(BigDecimal price) {
		super("Price must be greater than 0");
	}
	
}
