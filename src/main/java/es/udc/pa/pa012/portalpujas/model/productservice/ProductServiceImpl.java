package es.udc.pa.pa012.portalpujas.model.productservice;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa012.portalpujas.model.category.Category;
import es.udc.pa.pa012.portalpujas.model.category.CategoryDao;
import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.product.ProductDao;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfile;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfileDao;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidPriceException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidTimeException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private UserProfileDao userProfileDao;

	@Transactional(readOnly = true)
	public Product findProduct(Long productId) throws InstanceNotFoundException {
		System.out.println(productId);
		System.out.println(productDao);
		return productDao.find(productId);
	}

	public Product adInsertion(String description, int expirationTime,
			BigDecimal outPutPrice, String deliveryInfo, Long categoryId,
			Long userId, Calendar outPutDate, String name)
			throws InstanceNotFoundException, InvalidPriceException, InvalidTimeException{

		Category category = categoryDao.find(categoryId);

		UserProfile userProfile = userProfileDao.find(userId);
		
		if ((outPutPrice.compareTo(new BigDecimal("0")) == -1)){
			throw new InvalidPriceException(outPutPrice);
		}
		
		if(expirationTime < 0) {
			throw new InvalidTimeException(expirationTime);
		}
		
		Product product = new Product(description, expirationTime, outPutPrice,
				deliveryInfo, category, userProfile, outPutDate, name,
				outPutPrice, null, null);
		
		productDao.save(product);

		return product;

	}
	
	
	

	@Transactional(readOnly = true)
	public ProductBlock findProducts(String keywords, Long category,
			int startIndex, int count) {
		String g = keywords;
		
		if(keywords != null){
			if(g.contains("'")){
					g = g.replace("'", "");
			}
		}

		List<Product> products = productDao.fndByKeywords(g, category,
				startIndex, count + 1);

		Calendar now1 = Calendar.getInstance();
		for (Product product : products) {

			long d = product.getRemainedTime(now1, product.getOutPutDate());

			if (d > 0) {
				product.setExpirationTime((int) d);
			}
		}

		boolean existMoreProducts = products.size() == (count + 1);

		if (existMoreProducts) {
			products.remove(products.size() - 1);
		}
		System.out.println(products.toString());
		return new ProductBlock(products, existMoreProducts);
	}

	@Transactional(readOnly = true)
	public ProductBlock CheckAnnoucedProducts(Long userId, int startIndex,
			int count) throws InstanceNotFoundException {

		UserProfile usr = userProfileDao.find(userId);
		List<Product> products = productDao.findByUserId(usr.getUserProfileId(), startIndex,
				count + 1);

		Calendar now1 = Calendar.getInstance();
		for (Product product : products) {

			long d = product.getRemainedTime(now1, product.getOutPutDate());
			product.setExpirationTime((int) d);
		}

		boolean existMoreProducts = products.size() == (count + 1);

		if (existMoreProducts) {
			products.remove(products.size() - 1);
		}

		return new ProductBlock(products, existMoreProducts);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Category> FindCategories() {
		return categoryDao.findAll();
	}

	@Override
	public int countProducts(String keywords, Long category) {
		return productDao.countProducts(keywords, category);
	}

}
