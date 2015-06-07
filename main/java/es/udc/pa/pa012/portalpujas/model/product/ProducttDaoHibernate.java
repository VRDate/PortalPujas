package es.udc.pa.pa012.portalpujas.model.product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("productDao")
public class ProducttDaoHibernate extends GenericDaoHibernate<Product, Long>
		implements ProductDao {

	
	
	public int countProducts(String keywords, Long category) {
		Calendar now = Calendar.getInstance();
		String[] words = keywords != null ? keywords.toLowerCase().split(" ")
				: null;
		int resultado = 0;
		if (words == null) {
			if (category == null) {
				resultado = (int)(long) getSession().createQuery(
								"SELECT COUNT(a)"
										+ " FROM Product a WHERE a.outPutDate  BETWEEN :now AND a.outPutDate")
						.setParameter("now", now).uniqueResult();
			} else if (category != null) {
				resultado = (int)(long) getSession().createQuery(
								"SELECT count(b)"
										+ " FROM Product b WHERE b.productId.category.categoryId = :category "
										+ "AND b.productId.outPutDate  BETWEEN :now AND b.productId.outPutDate ORDER BY b.productId.category.name")
						.setParameter("category", category)
						.setParameter("now", now).uniqueResult();
			}
		} else {

			if (category == null) {

				String queryString = "SELECT count(b)"
						+ " FROM Product b WHERE b.outPutDate  BETWEEN :now AND b.outPutDate ";
				if (words.length > 0)
					queryString += " AND";
				String delim = " ";
				for (int i = 0; i < words.length; i++) {

					// System.out.println(words[i]);

					queryString += delim + " LOWER(b.name) LIKE '%" + words[i]
							+ "%'";
					delim = " AND ";

				}
				queryString += " ORDER BY b.productId";
				resultado = (int)(long) getSession().createQuery(queryString)
						.setParameter("now", now).uniqueResult();

			} else if (category != null) {
				String queryString = "SELECT count(b)"
						+ " FROM Product b WHERE b.category.categoryId = :category"
						+ " AND  b.outPutDate  BETWEEN :now AND b.outPutDate ";
				if (words.length > 0)
					queryString += " AND";
				String delim = " ";
				for (int i = 0; i < words.length; i++) {
					queryString += delim + " LOWER(b.name) LIKE '%" + words[i]
							+ "%'";
					delim = " AND ";

				}
				resultado = (int)(long) getSession().createQuery(queryString)
						.setParameter("category", category)
						.setParameter("now", now).uniqueResult();

			}
		}
		return resultado;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Product> findByUserId(Long user, int startIndex, int count) {

		return getSession()
				.createQuery(
						"SELECT a FROM Product a WHERE a.usr.userProfileId = :user "
								+ "ORDER BY a.outPutDate DESC")
				.setParameter("user", user).setFirstResult(startIndex)
				.setMaxResults(count).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> fndByKeywords(String keywords, Long category,
			int startIndex, int count) {
		Calendar now = Calendar.getInstance();
		String[] words = keywords != null ? keywords.toLowerCase().split(" ")
				: null;
		List<Product> resultado = new ArrayList<>();
		if (words == null) {
			if (category == null) {
				resultado = getSession()
						.createQuery(
								"SELECT a"
										+ " FROM Product a WHERE a.outPutDate  BETWEEN :now AND a.outPutDate")
						.setParameter("now", now).setFirstResult(startIndex)
						.setMaxResults(count).list();
			} else if (category != null) {
				resultado = getSession()
						.createQuery(
								"SELECT b"
										+ " FROM Product b WHERE b.productId.category.categoryId = :category "
										+ "AND b.productId.outPutDate  BETWEEN :now AND b.productId.outPutDate ORDER BY b.productId.category.name")
						.setParameter("category", category)
						.setParameter("now", now).setFirstResult(startIndex)
						.setMaxResults(count).list();
			}
		} else {

			if (category == null) {

				String queryString = "SELECT b"
						+ " FROM Product b WHERE b.outPutDate  BETWEEN :now AND b.outPutDate ";
				if (words.length > 0)
					queryString += " AND";
				String delim = " ";
				for (int i = 0; i < words.length; i++) {

					// System.out.println(words[i]);

					queryString += delim + " LOWER(b.name) LIKE '%" + words[i]
							+ "%'";
					delim = " AND ";

				}
				queryString += " ORDER BY b.productId";
				resultado = getSession().createQuery(queryString)
						.setParameter("now", now).setFirstResult(startIndex)
						.setMaxResults(count).list();

			} else if (category != null) {
				String queryString = "SELECT b"
						+ " FROM Product b WHERE b.category.categoryId = :category"
						+ " AND  b.outPutDate  BETWEEN :now AND b.outPutDate ";
				if (words.length > 0)
					queryString += " AND";
				String delim = " ";
				for (int i = 0; i < words.length; i++) {
					queryString += delim + " LOWER(b.name) LIKE '%" + words[i]
							+ "%'";
					delim = " AND ";

				}
				resultado = getSession().createQuery(queryString)
						.setParameter("category", category)
						.setParameter("now", now).setFirstResult(startIndex)
						.setMaxResults(count).list();

			}
		}
		return resultado;
	}

}