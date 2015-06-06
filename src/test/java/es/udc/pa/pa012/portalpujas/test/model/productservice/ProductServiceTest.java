package es.udc.pa.pa012.portalpujas.test.model.productservice;

import static es.udc.pa.pa012.portalpujas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa012.portalpujas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa012.portalpujas.model.bid.Bid;
import es.udc.pa.pa012.portalpujas.model.bid.BidDao;
import es.udc.pa.pa012.portalpujas.model.bidservice.BidBlock;
import es.udc.pa.pa012.portalpujas.model.bidservice.BidService;
import es.udc.pa.pa012.portalpujas.model.category.Category;
import es.udc.pa.pa012.portalpujas.model.category.CategoryDao;
import es.udc.pa.pa012.portalpujas.model.product.Product;
import es.udc.pa.pa012.portalpujas.model.product.ProductDao;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductBlock;
import es.udc.pa.pa012.portalpujas.model.productservice.ProductService;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfile;
import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfileDao;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidBidException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidPriceException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.InvalidTimeException;
import es.udc.pa.pa012.portalpujas.model.util.exceptions.ProductHasExpiredException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class ProductServiceTest {
	private final Calendar now = Calendar.getInstance();
	private final long NON_EXISTENT_USER_ID = -1;
	private final BigDecimal precio = new BigDecimal("50");

	@Autowired
	public ProductService productService;
	@Autowired
	public BidService bidService;
	@Autowired
	public ProductDao productDao;
	@Autowired
	public CategoryDao categoryDao;
	@Autowired
	public UserProfileDao userDao;
	@Autowired
	public BidDao bidDao;

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistantUser() throws InstanceNotFoundException {
		userDao.find(NON_EXISTENT_USER_ID);
	}

	@Test
	public void testFindProductById() throws InstanceNotFoundException {
		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);
		Category category = new Category("Libros");
		categoryDao.save(category);

		Product product = new Product("descripcion", 600,
				new BigDecimal("60.1"), "enviar a mi casa", category, usuario,
				Calendar.getInstance(), "La Metamorfosis", precio, null, null);
		productDao.save(product);

		assertEquals(product,
				productService.findProduct(product.getProductId()));

	}

	@Test(expected = ProductHasExpiredException.class)
	public void testBidForAExpiredProduct() throws InstanceNotFoundException,
			ProductHasExpiredException, InvalidBidException {
		Calendar date = Calendar.getInstance();
		date.set(2012, 12, 12);
		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);
		Category category = new Category("Libros");
		categoryDao.save(category);

		Product product = new Product("descripcion", 600,
				new BigDecimal("60.1"), "enviar a mi casa", category, usuario,
				date, "La Metamorfosis", precio, null, null);
		productDao.save(product);

		bidService.makeABid(new BigDecimal("1000"), usuario.getUserProfileId(),
				product.getProductId());
	}

	@Test
	public void testfindBidsByProductId() {
		List<Bid> bids = new ArrayList<>();
		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);
		Category category = new Category("Libros");
		categoryDao.save(category);

		Product product = new Product("descripcion", 600,
				new BigDecimal("60.1"), "enviar a mi casa", category, usuario,
				now, "La Metamorfosis", precio, null, null);
		productDao.save(product);

		Bid bid = new Bid(usuario, product, new BigDecimal("10"), now,
				product.getWinner(), product.getActualPrice());
		bidDao.save(bid);

		bids.add(bid);

		List<Bid> productBid = bidDao.findBidsByProductId(product
				.getProductId());
		assertEquals(bids, productBid);
	}

	@Test
	public void testFindUserProductsByUSerID() {

		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);

		UserProfile usuario2 = new UserProfile("Usuario1", "passUsuario",
				"Pepe", "Porras", "email@udc.es");
		userDao.save(usuario2);

		Category category = new Category("Libros");
		categoryDao.save(category);

		Product product = new Product("descripcion", 600,
				new BigDecimal("60.1"), "enviar a mi casa", category, usuario,
				now, "La Metamorfosis", precio, null, null);

		productDao.save(product);

		Bid bid = new Bid(usuario, product, new BigDecimal("15"),
				Calendar.getInstance(), product.getWinner(),
				product.getActualPrice());

		bidDao.save(bid);

		Product product2 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category, usuario2, now,
				"La Metamorfosis", precio, null, null);
		productDao.save(product2);

		List<Product> products = new ArrayList<>();
		products.add(product);

		// Obtenemos los productos del usuario Usuario
		List<Product> p = productDao.findByUserId(usuario.getUserProfileId(),
				0, 1);
		assertEquals(products, p);

	}

	// @Test
	// public void testFindfindMaxBidOfAProduct() throws
	// InstanceNotFoundException {
	// UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
	// "Porras", "email@udc.es");
	// userDao.save(usuario);
	//
	// UserProfile usuario2 = new UserProfile("Usuario1", "passUsuario",
	// "Pepe", "Porras", "email@udc.es");
	// userDao.save(usuario2);
	//
	// Category category = new Category("Libros");
	// categoryDao.save(category);
	//
	// Product product = new Product("descripcion", 600, new BigDecimal("60.1"),
	// "enviar a mi casa", category, usuario, now, "La Metamorfosis",
	// precio, null);
	// productDao.save(product);
	//
	// Bid bid = new Bid(usuario, product, new BigDecimal("65.0"), now);
	// bidDao.save(bid);
	// List<BigDecimal> bidMax = bidDao
	// .findMaxBidOfAProduct(product.getProductId());
	// Bid bid1 = bidDao.find(bid.getBidId());
	// if (bidMax.get(0) == bid1.getBidMax()) {
	// assertEquals(true, true);
	// }
	// }

	// @Test
	// public void testUpdateWinner() throws InstanceNotFoundException {
	// UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
	// "Porras", "email@udc.es");
	// userDao.save(usuario);
	// UserProfile usuario2 = new UserProfile("Usuario2", "passUsuario2",
	// "Paco2", "Porras2", "email2@udc.es");
	// userDao.save(usuario2);
	// Category category = new Category("Libros");
	// categoryDao.save(category);
	//
	// Product product = new Product("description", 200, new BigDecimal("50"),
	// "delivery",
	// category, usuario, now, "Reflex canon", precio,
	// usuario.getLoginName(), null);
	//
	// productDao.save(product);
	//
	// Product p = productDao.find(product.getProductId());
	//
	// product.setWinner(usuario2.getLoginName());
	//
	//
	//
	// List<Product> prod = productDao.findByUserId(
	// usuario.getUserProfileId(), 0, 2);
	// Product p2 = productDao.find(product.getProductId());
	//
	// assertEquals(p, p2);
	//
	// }

	@Test(expected = InvalidBidException.class)
	public void testInvalidBid() throws InvalidBidException,
			ProductHasExpiredException, InstanceNotFoundException {
		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);
		Category category = new Category("Libros");
		categoryDao.save(category);
		Product product = new Product("description", 500, new BigDecimal("50"),
				"delivery", category, usuario, now, "Reflex canon", precio,
				usuario.getLoginName(), null);
		productDao.save(product);
		System.out.println(product.toString());
		bidService.makeABid(new BigDecimal("1"), usuario.getUserProfileId(),
				product.getProductId());
	}

	@Test
	public void testAdInsertion() throws InstanceNotFoundException,InvalidPriceException , InvalidTimeException{

		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);

		Category category = new Category("Libros");
		categoryDao.save(category);

		// System.out.println(category.getCategoryId());

		Product insertion = productService.adInsertion("Libro1", 200,
				new BigDecimal("50.0"), "Info1", category.getCategoryId(),
				usuario.getUserProfileId(), now, "Libro1");
		Product product = productDao.find(insertion.getProductId());
		assertEquals(insertion, product);
	}

	@Test
	public void testfindProductsByKeywords() {
		/* FALTA COMPROBAR QUE NO DEVUELVA PRODUCTOS CADUCADOS */
		List<Product> products = new ArrayList<>();
		List<Product> products2 = new ArrayList<>();
		List<Product> products3 = new ArrayList<>();
		List<Product> products4 = new ArrayList<>();

		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);

		Category category = new Category("Libros");
		categoryDao.save(category);
		Category category1 = new Category("Ordenadores");
		categoryDao.save(category1);

		Product product = new Product("descripcion", 600,
				new BigDecimal("60.1"), "enviar a mi casa", category, usuario,
				now, "La Metamorfosis", precio, usuario.getLoginName(), null);
		productDao.save(product);
		Product product1 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category, usuario, now,
				"Juego de tronos", precio, usuario.getLoginName(), null);
		productDao.save(product1);
		Product product2 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category, usuario, now,
				"Cien años de soledad", precio, usuario.getLoginName(), null);
		productDao.save(product2);

		Product product3 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario, now,
				"Lenovo ideapad i7", precio, usuario.getLoginName(), null);
		productDao.save(product3);
		Product product4 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario, now,
				"Acer Aspire i5 ", precio, usuario.getLoginName(), null);
		productDao.save(product4);
		Product product5 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario, now,
				"MAc book Air i3", precio, usuario.getLoginName(), null);
		productDao.save(product5);

		Product product6 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario, now,
				"i5 MAc book Air Pro", precio, usuario.getLoginName(), null);
		productDao.save(product6);

		Bid bid = new Bid(usuario, product, new BigDecimal("10"), now,
				product.getWinner(), product.getActualPrice());
		bidDao.save(bid);
		Bid bid1 = new Bid(usuario, product1, new BigDecimal("10"), now,
				product1.getWinner(), product1.getActualPrice());
		bidDao.save(bid1);
		Bid bid2 = new Bid(usuario, product2, new BigDecimal("10"), now,
				product2.getWinner(), product2.getActualPrice());
		bidDao.save(bid2);

		products.add(product);
		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);
		products.add(product5);
		products.add(product6);

		int start = 0;
		int count = 0;
		// No se especifca nada, con lo cual se devuelve toda la informacion de
		// los productos.
		ProductBlock p1 = productService.findProducts(null, null, start, 7);

		assertEquals(products, p1.getProducts());

		// /*
		// * No se especifican palabras clave, pero si una categoria, con lo
		// cual
		// * se devuelve informacion de todos los porductos de dicha categoria
		// */
		// products2.add(product);
		// products2.add(product1);
		// products2.add(product2);
		//
		//
		// ProductBlock p4 =
		// productService.findProducts(null,category.getCategoryId(),start,count);
		// assertEquals(products2, p4.getProducts());
		//
		// // Buscamos especificando palabras clave y una categoria.
		// products3.add(product4);
		// products3.add(product6);
		//
		// ProductBlock p5 =
		// productService.findProducts("i5 acer",category1.getCategoryId(),0,2);
		// assertEquals(products3,p5.getProducts());
		// // Buscamos especificando palabras clave, pero no una categoria
		// products4.add(product);
		// products4.add(product1);
		// products4.add(product2);
		// products4.add(product3);
		// products4.add(product4);
		// products4.add(product6);
		//
		//
		//
		// ProductBlock p8 =
		// productService.findProducts("i5 acer de la",null,0,6);
		//
		// assertEquals(products4,p8.getProducts());
	}

	@Test
	public void testCheckAnnoucedProducts() throws ProductHasExpiredException, InstanceNotFoundException {
		int start = 0;
		int count = 1;
		List<Product> products = new ArrayList<>();
		List<Product> products2 = new ArrayList<>();

		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);

		UserProfile usuario2 = new UserProfile("Usuario2", "passUsuario",
				"Chapi", "To", "emai2@udc.es");
		userDao.save(usuario2);

		Category category = new Category("Libros");
		categoryDao.save(category);
		Category category1 = new Category("Ordenadores");
		categoryDao.save(category1);

		Product product = new Product("descripcion", 600,
				new BigDecimal("60.1"), "enviar a mi casa", category, usuario,
				now, "La Metamorfosis", precio, null, null);
		productDao.save(product);
		Product product1 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category, usuario, now,
				"Juego de tronos", precio, null, null);
		productDao.save(product1);
		Product product2 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category, usuario, now,
				"Cien años de soledad", precio, null, null);
		productDao.save(product2);

		Product product3 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario2, now,
				"Lenovo ideapad i7", precio, null, null);
		productDao.save(product3);
		Product product4 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario2, now,
				"Acer Aspire i5 ", precio, null, null);
		productDao.save(product4);
		Product product5 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario2, now,
				"MAc book Air i3", precio, null, null);
		productDao.save(product5);

		Product product6 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario2, now,
				"i5 MAc book Air Pro", precio, null, null);
		productDao.save(product6);

		products.add(product);
		products.add(product1);
		products.add(product2);

		ProductBlock p5 = productService.CheckAnnoucedProducts(
				usuario.getUserProfileId(), start, 3);

		assertEquals(products, p5.getProducts());

		products2.add(product3);
		products2.add(product4);
		products2.add(product5);
		products2.add(product6);

		ProductBlock p6 = productService.CheckAnnoucedProducts(
				usuario2.getUserProfileId(), start, 4);
		assertEquals(products2, p6.getProducts());

	}

	@Test
	public void makeAValidBid() throws InvalidBidException,
			ProductHasExpiredException, InstanceNotFoundException {
		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);
		UserProfile usuario2 = new UserProfile("Usuario2", "passUsuario2",
				"Maria", "Marras", "emai2@udc.es");
		userDao.save(usuario2);
		UserProfile usuario3 = new UserProfile("Usuario3", "passUsuario",
				"Ali", "Baba", "emai2@udc.es");
		userDao.save(usuario3);
		UserProfile usuario4 = new UserProfile("Usuario4", "passUsuario",
				"Jonny", "Walker", "emai4@udc.es");
		userDao.save(usuario4);
		Category category = new Category("Libros");
		categoryDao.save(category);
		Product product = new Product("description", 500, new BigDecimal("50"),
				"delivery", category, usuario, now, "Reflex canon",
				new BigDecimal("50"), usuario.getLoginName(), null);
		productDao.save(product);
		System.out.println(product.toString());

		/* Primera puja */
		bidService.makeABid(new BigDecimal("600"), usuario2.getUserProfileId(),
				product.getProductId());
		assertEquals(productDao.find(product.getProductId()).getWinner(),
				usuario2.getLoginName());
		assertEquals(product.getActualPrice(), new BigDecimal("50"));

		/* Segunda puja menor que la actual ganadora */
		bidService.makeABid(new BigDecimal("500"), usuario3.getUserProfileId(),
				product.getProductId());
		assertEquals(productDao.find(product.getProductId()).getWinner(),
				usuario2.getLoginName());
		assertEquals(product.getActualPrice(), new BigDecimal("500.5"));

		/* Tercera puja mayor que la acutal ganadora */
		/*
		 * El precioActual pasa a ser pujaganadora+0.5 por que hay alguien
		 * dispuesto a pagar más
		 */
		bidService.makeABid(new BigDecimal("800"), usuario4.getUserProfileId(),
				product.getProductId());
		assertEquals(productDao.find(product.getProductId()).getWinner(),
				usuario4.getLoginName());
		assertEquals(product.getActualPrice(), new BigDecimal("600.5"));

		/* Dos pujas ganadoras: gana la mas antigua */
		bidService.makeABid(new BigDecimal("800"), usuario3.getUserProfileId(),
				product.getProductId());
		assertEquals(productDao.find(product.getProductId()).getWinner(),
				usuario4.getLoginName());
		assertEquals(product.getActualPrice(), new BigDecimal("600.5"));

	}

	@Test
	public void testCheckBidStatus() throws InstanceNotFoundException {
		List<Bid> bids = new ArrayList<>();

		Calendar c = Calendar.getInstance();
		c.set(2000, 1, 1);
		Calendar c1 = Calendar.getInstance();
		c1.set(2000, 1, 1);
		Calendar c2 = Calendar.getInstance();
		c2.set(2000, 1, 3);
		Calendar c3 = Calendar.getInstance();
		c3.set(2000, 1, 4);
		Calendar c4 = Calendar.getInstance();
		c4.set(2000, 1, 5);
		Calendar c5 = Calendar.getInstance();
		c5.set(2000, 1, 6);

		UserProfile usuario = new UserProfile("Usuario", "passUsuario", "Paco",
				"Porras", "email@udc.es");
		userDao.save(usuario);

		UserProfile usuario2 = new UserProfile("Usuario2", "passUsuario",
				"Chapi", "To", "emai2@udc.es");
		userDao.save(usuario2);

		Category category = new Category("Libros");
		categoryDao.save(category);
		Category category1 = new Category("Ordenadores");
		categoryDao.save(category1);

		Product product = new Product("descripcion", 600,
				new BigDecimal("60.1"), "enviar a mi casa", category, usuario,
				now, "La Metamorfosis", precio, null, null);
		productDao.save(product);
		Product product1 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category, usuario, now,
				"Juego de tronos", precio, null, null);
		productDao.save(product1);
		Product product2 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category, usuario, now,
				"Cien años de soledad", precio, null, null);
		productDao.save(product2);
		Product product3 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario2, now,
				"Lenovo ideapad i7", precio, null, null);
		productDao.save(product3);
		Product product4 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario2, now,
				"Acer Aspire i5 ", precio, null, null);
		productDao.save(product4);
		Product product5 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario2, now,
				"MAc book Air i3", precio, null, null);
		productDao.save(product5);
		Product product6 = new Product("descripcion", 600, new BigDecimal(
				"60.1"), "enviar a mi casa", category1, usuario2, now,
				"i5 MAc book Air Pro", precio, null, null);
		productDao.save(product6);

		Bid bid = new Bid(usuario2, product, new BigDecimal("10"), c5,
				product.getWinner(), product.getActualPrice());
		bidDao.save(bid);
		Bid bid1 = new Bid(usuario2, product1, new BigDecimal("10"), c4,
				product1.getWinner(), product1.getActualPrice());
		bidDao.save(bid1);
		Bid bid2 = new Bid(usuario2, product2, new BigDecimal("10"), c3,
				product2.getWinner(), product2.getActualPrice());
		bidDao.save(bid2);
		Bid bid3 = new Bid(usuario2, product, new BigDecimal("10"), c2,
				product.getWinner(), product.getActualPrice());
		bidDao.save(bid3);
		Bid bid4 = new Bid(usuario2, product1, new BigDecimal("10"), c1,
				product1.getWinner(), product1.getActualPrice());
		bidDao.save(bid4);
		Bid bid5 = new Bid(usuario2, product2, new BigDecimal("10"), c,
				product2.getWinner(), product2.getActualPrice());
		bidDao.save(bid5);

		bids.add(bid);
		bids.add(bid1);
		bids.add(bid2);
		bids.add(bid3);
		bids.add(bid4);
		bids.add(bid5);

		BidBlock b = bidService.checkBidsStatus(usuario2.getUserProfileId(), 0,
				7);
		assertEquals(bids, b.getBids());

	}

	@Test
	public void testFindAllCategories() {

		Category category = new Category("Libros");
		categoryDao.save(category);
		Category category1 = new Category("Ordenadores");
		categoryDao.save(category1);

		List<Category> l = new ArrayList<>();
		l.add(category);
		l.add(category1);

		List<Category> categoies = productService.FindCategories();
		assertEquals(l, categoies);

	}

	// @Test
	// public void testMinutesBetween(){
	// Calendar c = Calendar.getInstance();
	// Calendar c1 = Calendar.getInstance();
	// Calendar c2 = Calendar.getInstance();
	// c.set(2015,0,0);
	// c1.set(2016,0,0);
	// c2.set(2014,0,0);
	//
	// long d =DateUtil.minutesBetween(c, c1);
	// long d1=DateUtil.minutesBetween(c, c2);
	// long r= 525600;
	// long r1=-525600;
	//
	// assertEquals(d,r);
	// assertEquals(d1,r1);
	//
	// }

}
