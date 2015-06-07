package es.udc.pa.pa012.portalpujas.model.util.exceptions;

import java.math.BigDecimal;

import es.udc.pa.pa012.portalpujas.model.bid.Bid;

@SuppressWarnings("serial")
public class InvalidBidException extends Exception {

	private Bid bid;

	public InvalidBidException(BigDecimal bid) {
		super("Invalid Bid, your bid must be greater");
	}

	public Bid getBid() {
		return bid;
	}

	public void setBid(Bid bid) {
		this.bid = bid;
	}

}
