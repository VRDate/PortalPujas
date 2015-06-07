package es.udc.pa.pa012.portalpujas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;

public class ProductGridDatasource implements GridDataSource{

	
	private int startIndex;
	private List<Product> products;
	private String keywords;
	private Long categoryId;
	private ProductService pService;
	
	public ProductGridDatasource(int startIndex, 
			String keywords, Long categoryId, ProductService pService) {
		this.startIndex = startIndex;
		this.keywords = keywords;
		this.categoryId = categoryId;
		this.pService = pService;
	}

	@Override
	public int getAvailableRows() {
		return pService.countProducts(keywords, categoryId);
	}

	@Override
	public Class<Product> getRowType() {
		return Product.class;
	}

	@Override
	public Object getRowValue(int index) {
		return products.get(index-startIndex);
	}

	@Override
	public void prepare(int startIndex, int endIndex, List<SortConstraint> arg2) {
			products = pService.findProducts(keywords, categoryId, startIndex, endIndex-startIndex +1).getProducts();
			this.startIndex = startIndex;		
	}

	
	
}
