package es.udc.pa.pa012.portalpujas.model.category;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("categoryDao")
public class CategoryDaoHibernate extends GenericDaoHibernate<Category, Long>
		implements CategoryDao {

	@SuppressWarnings("unchecked")
	public List<Category> findAll() {
		return getSession().createQuery(
				"SELECT a FROM Category a ORDER BY a.name").list();
	}

}
