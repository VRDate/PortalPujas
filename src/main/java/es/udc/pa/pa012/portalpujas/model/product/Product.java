package es.udc.pa.pa012.portalpujas.model.product;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import es.udc.pa.pa012.portalpujas.model.bid.Bid;
import es.udc.pa.pa012.portalpujas.model.category.Category;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfile;

@Entity
@org.hibernate.annotations.BatchSize(size = 10)
public class Product {

	private Long productId;
	private String description;
	private int expirationTime;
	private BigDecimal outPutPrice;
	private String deliveryInfo;
	private Category category;
	private UserProfile usr;
	private Calendar outPutDate;
	private String name;
	private BigDecimal actualPrice;
	private String winner;
	private Bid bidMax;
	private long version;

	public Product() {
	}

	public Product(String description, int expirationTime,
			BigDecimal outPutPrice, String deliveryInfo, Category category,
			UserProfile usr, Calendar outPutDate, String name,
			BigDecimal actualPrice, String winner, Bid bidMax) {

		this.description = description;
		this.expirationTime = expirationTime;
		this.outPutPrice = outPutPrice;
		this.deliveryInfo = deliveryInfo;
		this.category = category;
		this.usr = usr;
		outPutDate.add(Calendar.MINUTE, expirationTime);
		this.outPutDate = outPutDate;
		this.name = name;
		this.actualPrice = actualPrice;
		this.winner = winner;
		// BigDecimal bid = outPutPrice;
		// bidMax.setBidMax(bid);
		this.bidMax = bidMax;

	}

	@SequenceGenerator( // It only takes effect for
			name = "ProductIdGenerator", // databases providing identifier
			sequenceName = "ProductSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,
			generator = "ProductIdGenerator")
	public Long getProductId() {
		return productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(int expirationTime) {
		this.expirationTime = expirationTime;
	}

	public BigDecimal getOutPutPrice() {
		return outPutPrice;
	}

	public void setOutPutPrice(BigDecimal outPutPrice) {
		this.outPutPrice = outPutPrice;
	}

	public String getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(String deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "usrId")
	public UserProfile getUsr() {
		return usr;
	}

	public void setUsr(UserProfile usr) {
		this.usr = usr;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getOutPutDate() {
		return outPutDate;
	}

	public void setOutPutDate(Calendar outPutDate) {
		this.outPutDate = outPutDate;
	}

	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getWinner() {

		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	@Version
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "bidMax")
	public Bid getBidMax() {
		return bidMax;
	}

	public void setBidMax(Bid bidMax) {
		this.bidMax = bidMax;
	}

//	@Override
//	public String toString() {
//
//		SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//		return "Product [productId=" + productId + ", description="
//				+ description + ", expirationTime=" + expirationTime
//				+ ", outPutPrice=" + outPutPrice + ", deliveryInfo="
//				+ deliveryInfo + ", category=" + category + ", usr=" + usr
//				+ ", outPutDate=" + s.format(outPutDate.getTime()) + ", name="
//				+ name + ", actualPrice=" + actualPrice + ", winner=" + winner
//				+ ", bidMax=" + bidMax + ", version=" + version + "]";
//	}

	
	
	
	@Temporal(TemporalType.TIMESTAMP)
	public long getRemainedTime(Calendar startDate, Calendar endDate) {
		long end = endDate.getTimeInMillis();
		long start = startDate.getTimeInMillis();
		return TimeUnit.MILLISECONDS.toMinutes((end - start));
	}


}
