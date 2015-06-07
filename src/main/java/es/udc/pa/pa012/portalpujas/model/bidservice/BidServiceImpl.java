package es.udc.pa.pa012.portalpujas.model.bidservice;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa012.portalpujas.model.bid.Bid;
import es.udc.pa.pa012.portalpujas.model.bid.BidDao;
import es.udc.pa.pa012.portalpujas.model.category.CategoryDao;
import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.product.ProductDao;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfile;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfileDao;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidBidException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.ProductHasExpiredException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("bidService")
/* Used for register a class into the ApplicationContext */
@Transactional
public class BidServiceImpl implements BidService {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private UserProfileDao userDao;
	@Autowired
	private BidDao bidDao;


	@Override
	@Transactional(readOnly = true)
	public BidBlock checkBidsStatus(Long bidder, int startIndex, int count) throws InstanceNotFoundException {

		/*
		 * Find count+1 bids to determine if there exist more bids above the
		 * specified range.
		 */
		UserProfile usr = userDao.find(bidder);
		
		List<Bid> bids = bidDao.findByBidder(usr.getUserProfileId(), startIndex, count + 1);

		boolean existMoreBids = bids.size() == (count + 1);

		/*
		 * Remove the last bids from the returned list if there exist more
		 * accounts above the specified range.
		 */
		if (existMoreBids) {
			bids.remove(bids.size() - 1);
		}

		/* Return BidBlock. */
		return new BidBlock(bids, existMoreBids);

	}

	@Override
	public Bid makeABid(BigDecimal userBid, Long userId, Long productId)
			throws ProductHasExpiredException, InvalidBidException,
			InstanceNotFoundException {

		Calendar now = Calendar.getInstance();

		Product product = productDao.find(productId);
		UserProfile user = userDao.find(userId);

		if (product == null)
			throw new InstanceNotFoundException(productId, "productId");

		if (user == null)
			throw new InstanceNotFoundException(productId, "productId");

		if (product.getOutPutDate().before(now)) {
			throw new ProductHasExpiredException(product);
		}

		if (product.getBidMax() == null) {
			BigDecimal actual = product.getActualPrice();
			if (actual.compareTo(userBid) <=0) {
				product.setWinner(user.getLoginName());
				Bid firstBid = new Bid(user, product, userBid,
						Calendar.getInstance(), product.getWinner(),
						product.getActualPrice());
				bidDao.save(firstBid);
				product.setBidMax(firstBid);
			} else {
				throw new InvalidBidException(userBid);
			}
		} else {
			if (userBid.compareTo(product.getActualPrice()) != 1) {
				throw new InvalidBidException(userBid);
			} else {
				BigDecimal prodctBidMax = product.getBidMax().getBidMax();
				BigDecimal actualPrice = product.getOutPutPrice();
				/*
				 * Puja menor que la puja ganadora pero mayor que el precio
				 * actual
				 */
				if (userBid.compareTo(prodctBidMax) == -1) {
					Bid anotherBid = new Bid(user, product, userBid,
							Calendar.getInstance(), product.getWinner(),
							product.getActualPrice());
					bidDao.save(anotherBid);
					product.setActualPrice(userBid.add(new BigDecimal("0.5")));

				}
				/* Puja mayor que la puja ganadora y que el precio actual */
				if (userBid.compareTo(prodctBidMax) == 1) {
					Bid anotherBidMore = new Bid(user, product, userBid,
							Calendar.getInstance(), product.getWinner(),
							product.getActualPrice());
					bidDao.save(anotherBidMore);
					product.setActualPrice(prodctBidMax.add(new BigDecimal(
							"0.5")));
					product.setWinner(user.getLoginName());
					product.setBidMax(anotherBidMore);
				}
				/*
				 * Si hay dos pujas ganadoras gana la más antigua. No se
				 * actualiza ni el ganador ni el precio
				 */
				BigDecimal bm = product.getBidMax().getBidMax();
				if (userBid.compareTo(prodctBidMax) == 0) {
					Bid sameBid = new Bid(user, product, userBid,
							Calendar.getInstance(), product.getWinner(),
							product.getActualPrice());
					bidDao.save(sameBid);
				}
			}

		}
		return null;
	}

}
