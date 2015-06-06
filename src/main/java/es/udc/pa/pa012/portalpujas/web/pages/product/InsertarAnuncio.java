package es.udc.pa.pa012.portalpujas.web.pages.product;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;

import com.google.javascript.rhino.head.json.JsonParser.ParseException;

import es.udc.pa.pa012.portalpujas.model.bid.Bid;
import es.udc.pa.pa012.portalpujas.model.category.Category;
import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidPriceException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidTimeException;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicy;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa012.portalpujas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class InsertarAnuncio {

	@Property
	@SessionState(create = false)
	private UserSession userSession;
	@Property
	private Long productId;

	@InjectPage
	private ProductDetails productDetails;

	@Property
	private String description;
	@Property
	private int expirationTime;
	@Property
	private BigDecimal outPutPrice;
	@Property
	private String deliveryInfo;
	@Property
	private String name;
	@Property
	private Bid bidMax;

	@Component(id = "outPutPrice")
	private TextField outPutPriceField;
	@Component(id="expirationTime")
	private TextField expirationTimeField;
	
	@Property
	private String[] categoriaSelectModel;

	@Component
	private Form adInsertionForm;

	@Inject
	private ProductService productService;
	@Property
	private String categorys = "";
	@Property
	private Long category;
	@Inject
	private Messages messages;

	Object onValidateFromAdInsertionForm() throws InstanceNotFoundException, InvalidPriceException,InvalidTimeException {

		try{
		Product product = productService.adInsertion(description,
				expirationTime, outPutPrice, deliveryInfo, category,
				userSession.getUserProfileId(), Calendar.getInstance(), name);

		productDetails.setProductId(product.getProductId());
		return productDetails;
		}catch(InvalidPriceException e){
			adInsertionForm.recordError(outPutPriceField, messages.get("InvalidPrice"));
		}catch(InvalidTimeException e){
			adInsertionForm.recordError(expirationTimeField, messages.get("nvalidTime"));
	
		}
		return null;

	}

	void onActivate() throws ParseException {

		List<Category> lista = productService.FindCategories();

		for (Category category : lista) {
			categorys += category.getCategoryId() + "=" + category.getName()
					+ ",";
		}
	}
}
