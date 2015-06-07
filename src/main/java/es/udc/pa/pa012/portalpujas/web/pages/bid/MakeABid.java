package es.udc.pa.pa012.portalpujas.web.pages.bid;

import java.math.BigDecimal;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa012.portalpujas.model.bidservice.BidService;
import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidBidException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.ProductHasExpiredException;
import es.udc.pa.pa012.portalpujas.web.pages.SuccessfulOperation;
import es.udc.pa.pa012.portalpujas.web.pages.user.Login;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicy;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa012.portalpujas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class MakeABid {
	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	PageRenderLinkSource linkSource;

	private Long productId;

	private String name;

	private Product product;

	private BigDecimal price;

	private String winner;

	@Property
	private BigDecimal bidMax;

	@Component(id = "bidMax")
	private TextField bidMaxField;

	@Property
	private boolean bidStatus = false;

	@Component
	private Form ajaxForm;

	@Inject
	private BidService bidService;

	@Inject
	private ProductService productService;

	@Inject
	private Messages messages;

	@InjectPage
	private SuccessfulOperation succes;

	@InjectComponent
	private Zone formZone;

	@InjectComponent
	private Zone outZone;
	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectPage
	private Login login;

	@Property
	private String link;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public boolean getIsWinner() {
		if (product.getWinner() != null)
			return true;
		else
			return false;
	}

	void onValidateFromAjaxForm() throws InstanceNotFoundException,
			ProductHasExpiredException, InvalidBidException {

		if (bidMax == null) {
			ajaxForm.recordError(bidMaxField, "Debe pujar una cantidad");
		} else {
			try {
				bidService.makeABid(bidMax, userSession.getUserProfileId(),
						productId);
				bidStatus = true;
			} catch (InvalidBidException e) {
				ajaxForm.recordError(bidMaxField, messages.get("pujasuperior"));
			} catch (ProductHasExpiredException e) {
				ajaxForm.recordError(bidMaxField,
						messages.get("productexpired"));
			}
		}
	}

	void onSuccess() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone).addRender(outZone);
		}
	}

	void onFailure() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone);
		}
	}

	void onActivate(Long productId) {
		this.productId = productId;
		try {
			product = productService.findProduct(productId);
		} catch (InstanceNotFoundException e) {

		}

	}

	Object onPassivate() {
		return new Object[] { productId, name, price, winner };
	}
}
