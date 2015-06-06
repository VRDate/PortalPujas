package es.udc.pa.pa012.portalpujas.web.pages.product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.jboss.logging.Messages;

import com.google.javascript.rhino.head.json.JsonParser.ParseException;

import es.udc.pa.pa012.portalpujas.model.category.Category;
import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductBlock;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;
import es.udc.pa.pa012.portalpujas.web.util.ProductGridDatasource;

public class FindProducts {
	static private final int MAX_RESULTS = 30;

	@Property
	private Long id;
	@Persist
	@Property
	private Long categoryId;
	@Persist
	@Property
	private String keywords;

	@Property
	private Product product;

	@InjectPage
	private ProductDetails productDetails;

	@Inject
	private ProductService productService;

	@Property
	private String categorys = "";
	@Property
	private Long category;


	@InjectComponent
	private Zone formZone;

	@Inject
	private Request request;
	@Property
	private boolean result = false;
//	@Property
//	private GridDataSource products;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	List<String> onProvideCompletionsFromKeywords(String keywords) {
		System.out.println(categoryId + "ERTRETERTERTER");
		ProductBlock matches = productService.findProducts(keywords,
				categoryId, 0, 10);

		List<Product> pro = matches.getProducts();

		List<String> result = new ArrayList<String>();

		for (Product a : pro) {
			result.add(a.getName());
			System.out.println(a.getName());
		}

		return result;
	}

	
	public GridDataSource getProducts(){
		
		return new ProductGridDatasource(0, keywords, categoryId, productService);
	}
	
	
	void onValidateFromAdInsertionForm() {
		result = true;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone);
		}
	}

	void onActivate() throws ParseException {

		List<Category> lista = productService.FindCategories();

		for (Category category : lista) {
			categorys += category.getCategoryId() + "=" + category.getName()
					+ ",";
		}
	}

}
