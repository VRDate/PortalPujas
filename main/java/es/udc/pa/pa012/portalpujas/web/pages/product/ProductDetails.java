package es.udc.pa.pa012.portalpujas.web.pages.product;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;

import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;
import es.udc.pa.pa012.portalpujas.web.pages.bid.MakeABid;
import es.udc.pa.pa012.portalpujas.web.pages.user.Login;
import es.udc.pa.pa012.portalpujas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ProductDetails {

	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private Product product;

	@Property
	private Long productId;

	@Property
	private String name;

	@Property
	private int x;


	@Component
	private Form productForm;

	@Inject
	private ProductService productService;

	@InjectPage
	private MakeABid makeABid;
	@InjectPage
	private Login login;

	@Inject
	private Locale locale;

	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}

	public boolean getWinner() {
		if (product.getWinner() != null)
			return true;
		else
			return false;
	}

	public String getOutPutDate() {
		Calendar d = product.getOutPutDate();
		int e = product.getExpirationTime();
		d.add(Calendar.MINUTE, -e);
		return format.format(d.getTime());
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getProductId() {
		return productId;
	}


	public long getTime() {
		Calendar d = product.getOutPutDate();
		int e = product.getExpirationTime();
		d.add(Calendar.MINUTE, e);
		return product.getRemainedTime(Calendar.getInstance(),
				d);
	}

	public boolean getExpired() {

		Long i = product.getRemainedTime(Calendar.getInstance(),
				product.getOutPutDate());
		if (i < 0)
			return false;
		else
			return true;
	}
	
	
	public boolean getBefore(){
		return product.getOutPutDate().before(Calendar.getInstance());
	}

	Object onValidateFromProductForm() {
		makeABid.setProductId(product.getProductId());
		makeABid.setName(product.getName());
		makeABid.setPrice(product.getActualPrice());
		makeABid.setWinner(product.getWinner());
		if (userSession == null) {
			login.setProductId(product.getProductId());
			login.setName(product.getName());
			login.setPrice(product.getActualPrice());
			login.setWinner(product.getWinner());
			return login;
		} else
			return makeABid;
	}

	void onActivate(Long productId) {
		this.productId = productId;
		try {
			product = productService.findProduct(productId);
		} catch (InstanceNotFoundException e) {

		}
	}

	Long onPassivate() {
		return productId;
	}

}
