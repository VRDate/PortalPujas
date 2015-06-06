package es.udc.pa.pa012.portalpujas.web.pages.user;

import java.math.BigDecimal;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfile;
import es.udc.pa.pa012.portalpujas.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa012.portalpujas.model.userservice.UserService;
import es.udc.pa.pa012.portalpujas.web.pages.Index;
import es.udc.pa.pa012.portalpujas.web.pages.bid.MakeABid;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicy;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa012.portalpujas.web.util.CookiesManager;
import es.udc.pa.pa012.portalpujas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

	private Long productId;

	private String name;

	private Product product;

	private BigDecimal price;

	private String winner;

	@Property
	private String loginName;

	@Property
	private String password;

	@Property
	private boolean rememberMyPassword;

	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private Cookies cookies;

	@Component
	private Form loginForm;

	@Inject
	private Messages messages;

	@Inject
	private UserService userService;

	private UserProfile userProfile = null;

	@InjectPage
	private MakeABid makeABid;

	@Inject
	private ProductService productService;

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	void onValidateFromLoginForm() {

		if (!loginForm.isValid()) {
			return;
		}

		try {
			userProfile = userService.login(loginName, password, false);
		} catch (InstanceNotFoundException e) {
			loginForm.recordError(messages.get("error-authenticationFailed"));
		} catch (IncorrectPasswordException e) {
			loginForm.recordError(messages.get("error-authenticationFailed"));
		}

	}

	Object onSuccess() {

		userSession = new UserSession();
		userSession.setUserProfileId(userProfile.getUserProfileId());
		userSession.setFirstName(userProfile.getFirstName());

		if (rememberMyPassword) {
			CookiesManager.leaveCookies(cookies, loginName,
					userProfile.getEncryptedPassword());
		}

		if (productId != null) {
			makeABid.setProductId(productId);
			makeABid.setName(name);
			makeABid.setPrice(price);
			makeABid.setWinner(winner);
			return makeABid;
		} else
			return Index.class;

	}

	// void onActivate(String bidContext) {
	// this.bidContext = bidContext;
	// }

	// Object[] onPassivte() {
	// return new Object[] { bidContext };
	// }

	void onActivate(Long productId, String name, BigDecimal price, String winner) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.winner = winner;
	}

	Object[] onPassivate() {
		return new Object[] { productId, name, price, winner };
	}

}
