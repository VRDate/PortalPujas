package es.udc.pa.pa012.portalpujas.model.bid;

import java.math.BigDecimal;
import java.util.List;

import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pojo.modelutil.dao.GenericDao;

public interface BidDao extends GenericDao<Bid, Long> {

	public List<Bid> findByBidder(Long bidder, int startIndex, int count);

	public List<Bid> findBidsByProductId(Long productid);
	// public List<BigDecimal> findMaxBidOfAProduct(Long product);
}
