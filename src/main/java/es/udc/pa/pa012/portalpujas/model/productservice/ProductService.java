package es.udc.pa.pa012.portalpujas.model.productservice;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import es.udc.pa.pa012.portalpujas.model.category.Category;
import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidPriceException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidTimeException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface ProductService {

	/* Insert an product advertisement */
	public Product adInsertion(String description, int expirationTime,
			BigDecimal outPutPrice, String deliveryInfo, Long categoryId,
			Long userId, Calendar outPutDate, String name)
			throws InstanceNotFoundException, InvalidPriceException, InvalidTimeException;

	public ProductBlock findProducts(String keywords, Long category,
			int startIndex, int count);

	/* Shows user announced products over the time */
	public ProductBlock CheckAnnoucedProducts(Long userId, int startIndex,
			int count) throws InstanceNotFoundException;

	public List<Category> FindCategories();

	public Product findProduct(Long productId) throws InstanceNotFoundException;
	
	public int countProducts(String keywords, Long category);
}
