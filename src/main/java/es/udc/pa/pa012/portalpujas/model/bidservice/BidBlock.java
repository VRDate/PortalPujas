package es.udc.pa.pa012.portalpujas.model.bidservice;

import java.util.List;

import es.udc.pa.pa012.portalpujas.model.bid.Bid;

public class BidBlock {

	private List<Bid> bids;
	private boolean existMoreBids;

	public BidBlock(List<Bid> bids, boolean existMoreBids) {

		this.bids = bids;
		this.existMoreBids = existMoreBids;

	}

	public List<Bid> getBids() {
		return bids;
	}

	public boolean getExistMoreBids() {
		return existMoreBids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public void setExistMoreBids(boolean existMoreBids) {
		this.existMoreBids = existMoreBids;
	}

}
