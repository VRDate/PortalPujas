package es.udc.pa.pa012.portalpujas.model.product;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface ProductDao extends GenericDao<Product, Long> {
	/**
	 * Returns a list of products pertaining to a given user. If the user has no
	 * products, an empty list is returned.
	 *
	 * @param userId
	 *            the user identifier
	 * @param startIndex
	 *            the index (starting from 0) of the first account to return
	 * @param count
	 *            the maximum number of accounts to return
	 * @return the list of accounts
	 * @throws InstanceNotFoundException
	 */
	public List<Product> findByUserId(Long userId, int startIndex, int count);

	public int countProducts (String keywords, Long category);
	
	public List<Product> fndByKeywords(String keywords, Long categoryId,
			int startIndex, int count);

}
