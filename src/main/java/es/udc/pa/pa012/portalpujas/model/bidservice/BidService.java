package es.udc.pa.pa012.portalpujas.model.bidservice;

import java.math.BigDecimal;

import es.udc.pa.pa012.portalpujas.model.bid.Bid;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidBidException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.ProductHasExpiredException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface BidService {
	/* Make a Bid */
	public Bid makeABid(BigDecimal userBid, Long userId, Long porductId)
			throws ProductHasExpiredException, InvalidBidException,
			InstanceNotFoundException;

	/* Shows the bids associated to an authenticated user over the time */
	public BidBlock checkBidsStatus(Long bidder, int startIndex, int count) throws InstanceNotFoundException;
}
