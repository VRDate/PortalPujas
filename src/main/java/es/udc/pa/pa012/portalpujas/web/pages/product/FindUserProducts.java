package es.udc.pa.pa012.portalpujas.web.pages.product;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;

import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductBlock;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicy;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa012.portalpujas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class FindUserProducts {

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private Product product;

	private final static int PRODUCTS_PER_PAGE = 10;
	private int startIndex = 0;
	private ProductBlock productBlock;
	private String keywords;
	private Long time;

	@Inject
	private ProductService productService;

	@InjectComponent
	private Zone loopZone;

	@Inject
	private Request request;

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {

		return product.getRemainedTime(Calendar.getInstance(),
				product.getOutPutDate());
	}

	public List<Product> getProducts() {
		// System.out.println(productBlock.getProducts().size());
		return productBlock.getProducts();
	}

	Object onActionFromRefreshZonePrev() throws InstanceNotFoundException {
		if (startIndex - PRODUCTS_PER_PAGE >= 0) {
			this.startIndex = startIndex - PRODUCTS_PER_PAGE;
			productBlock = productService.CheckAnnoucedProducts(
					userSession.getUserProfileId(), startIndex,
					PRODUCTS_PER_PAGE);
		}
		// Here we can do whatever updates we want, then return the content we
		// want rendered.
		return request.isXHR() ? loopZone.getBody() : null;
	}

	Object onActionFromRefreshZoneNext() throws InstanceNotFoundException {
		if (productBlock.isExistMoreProducts()) {
			this.startIndex = startIndex + PRODUCTS_PER_PAGE;
			productBlock = productService.CheckAnnoucedProducts(
					userSession.getUserProfileId(), startIndex,
					PRODUCTS_PER_PAGE);
		}
		// Here we can do whatever updates we want, then return the content we
		// want rendered.
		return request.isXHR() ? loopZone.getBody() : null;
	}

	public Object[] getPreviousLinkContext() {

		if (startIndex - PRODUCTS_PER_PAGE >= 0) {
			return new Object[] { startIndex - PRODUCTS_PER_PAGE,
					userSession.getUserProfileId(), startIndex };
		} else {
			return null;
		}

	}

	public Object[] getNextLinkContext() {

		if (productBlock.isExistMoreProducts()) {
			return new Object[] { startIndex + PRODUCTS_PER_PAGE,
					userSession.getUserProfileId(), startIndex };
		} else {
			return null;
		}

	}

	public boolean getWinner() {
		if (product.getWinner() != null)
			return true;
		else
			return false;
	}

	public boolean getExpired() {
		if (product.getExpirationTime() > 0)
			return true;
		else
			return false;
	}

	int onPassivate() {
		return startIndex;
	}

	void onActivate(int startIndex) throws InstanceNotFoundException {
		this.startIndex = startIndex;
		productBlock = productService.CheckAnnoucedProducts(
				userSession.getUserProfileId(), startIndex, PRODUCTS_PER_PAGE);
	}
}
