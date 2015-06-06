package es.udc.pa.pa012.portalpujas.model.bid;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("bidDao")
public class BidDaoHibernate extends GenericDaoHibernate<Bid, Long> implements
		BidDao {

	@SuppressWarnings("unchecked")
	public List<Bid> findByBidder(Long bidder, int startIndex, int count) {

		return getSession()
				.createQuery(
						"SELECT a FROM Bid a WHERE a.bidder.userProfileId = :bidder "
								+ "ORDER BY a.bidTime DESC")
				.setParameter("bidder", bidder).setFirstResult(startIndex)
				.setMaxResults(count).list();

	}

	@SuppressWarnings("unchecked")
	public List<Bid> findBidsByProductId(Long product) {
		return getSession()
				.createQuery(
						"SELECT a FROM Bid a WHERE a.product.productId = :product")
				.setParameter("product", product).list();
	}

}
