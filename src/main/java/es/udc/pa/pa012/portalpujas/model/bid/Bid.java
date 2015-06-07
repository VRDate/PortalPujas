package es.udc.pa.pa012.portalpujas.model.bid;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfile;

@Entity
@org.hibernate.annotations.BatchSize(size = 10)
public class Bid {

	private Long bidId;
	private UserProfile bidder;
	private Product product;
	private BigDecimal bidMax;
	private Calendar bidTime;
	private String instantWinner;
	private BigDecimal instantPrice;

	public Bid() {
	}

	public Bid(UserProfile bidder, Product product, BigDecimal bidMax,
			Calendar bidTime, String instantWinner, BigDecimal instantPrice) {

		/**
		 * NOTE: "bidId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.bidder = bidder;
		this.product = product;
		this.bidMax = bidMax;
		this.bidTime = bidTime;
		this.instantWinner = instantWinner;
		this.instantPrice = instantPrice;

	}

	@SequenceGenerator( // It only takes effect for
			name = "BidIdGenerator", // databases providing identifier
			sequenceName = "BidSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,
			generator = "BidIdGenerator")
	public Long getBidId() {
		return bidId;
	}

	public void setBidId(Long bidId) {
		this.bidId = bidId;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "bidder")
	public UserProfile getBidder() {
		return bidder;
	}

	public void setBidder(UserProfile bidder) {
		this.bidder = bidder;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getBidMax() {
		return bidMax;
	}

	public void setBidMax(BigDecimal bidMax) {
		this.bidMax = bidMax;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getBidTime() {
		return bidTime;
	}

	public void setBidTime(Calendar bidTime) {
		this.bidTime = bidTime;
	}

	public String getInstantWinner() {
		return instantWinner;
	}

	public void setInstantWinner(String instantWinner) {
		this.instantWinner = instantWinner;
	}

	public BigDecimal getInstantPrice() {
		return instantPrice;
	}

	public void setInstantPrice(BigDecimal instantPrice) {
		this.instantPrice = instantPrice;
	}


//	@Override
//	public String toString() {
//		SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//		return "Bid [bidId=" + bidId + ", bidder=" + bidder + ", product="
//				+ product + ", bidMax=" + bidMax + ", bidTime="
//				+ s.format(bidTime.getTime()) + ", instantWinner="
//				+ instantWinner + ", instantPrice=" + instantPrice + "]";
//	}

	
	
}
