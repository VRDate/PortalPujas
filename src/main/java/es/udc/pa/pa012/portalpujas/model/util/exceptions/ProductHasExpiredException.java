package es.udc.pa.pa012.portalpujas.model.util.exceptions;

import es.udc.pa.pa012.portalpujas.model.product.Product;

@SuppressWarnings("serial")
public class ProductHasExpiredException extends Exception {

	private Product product;

	public ProductHasExpiredException(Product product) {
		super("Product " + product.getName() + " has expired");
		setProduct(product);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
