package app.model;

import java.util.List;

import models.Product;
import util.MongoDBUtil;

import com.mongodb.BasicDBObject;

public class ProductTest extends AbstractMongoDBTest {
	
	private Product product = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MongoDBUtil.setColl(getDBCollection());
	}

	
	public void testGetProductIds() {
		getDBCollection().save(new BasicDBObject("id", 1234));
		getDBCollection().save(new BasicDBObject("id", 1235));
		getDBCollection().save(new BasicDBObject("id", 1236));
		getDBCollection().save(new BasicDBObject("id", 1334));
		
		List<Integer> ids = Product.getProductIds(123);
		
		assertEquals(3, ids.size());
		assertTrue(ids.contains(1234));
		assertTrue(ids.contains(1235));
		assertTrue(ids.contains(1236));
	}
	
	public void testGetProduct() {
		getDBCollection().save(new BasicDBObject("id", 1234)
		.append("title", "Sugar")
		.append("pricing", new BasicDBObject("cost", 10.5d).append("price",12d).append("promo_price", 0.0d)));
		getDBCollection().save(new BasicDBObject("id", 1334));
		
		Product obj = Product.findById(1234);
		
		
		
		System.out.println("***********" + product);
		
		assertNotNull(obj);
		assertEquals(1234, obj.id.intValue());
		assertEquals("Sugar", obj.title);
		assertEquals(10.5d, obj.pricing.cost);
		assertEquals(12d, obj.pricing.price);
		
	}
	
	public void testUpdateProduct() { 
		getDBCollection().save(new BasicDBObject("id", 1234)
		.append("title", "Sugar")
		.append("pricing", new BasicDBObject("cost", 10.5).append("price",12)));
		getDBCollection().save(new BasicDBObject("id", 1334));
		
		Product product = Product.findById(1234);
		product.pricing.price = 13.0d;
		product.title = "Sugar 2";
		
		product.updatePriceAndTitle();
		
		Product obj = Product.findById(1234);
		
		assertNotNull(obj);
		assertEquals(1234, obj.id.intValue());
		assertEquals("Sugar 2", obj.title);
		assertEquals(10.5d, obj.pricing.cost);
		assertEquals(13d, obj.pricing.price);
		
	}
	
	public void testUpdateProductInvalidTitle() {
		getDBCollection().save(new BasicDBObject("id", 1234)
		.append("title", "Sugar")
		.append("pricing", new BasicDBObject("cost", 10.5d).append("price",12d)));
		getDBCollection().save(new BasicDBObject("id", 1334));
		
		Product product = Product.findById(1234);
		product.title = "";
		
		try {
			product.updatePriceAndTitle();
		} catch(Exception e) {
			assertEquals("product.title.blank", e.getMessage());
		}
		Product obj = Product.findById(1234);
		
		assertNotNull(obj);
		assertEquals(1234, obj.id.intValue());
		assertEquals("Sugar", obj.title);
		assertEquals(10.5d, obj.pricing.cost);
		assertEquals(12d, obj.pricing.price);
	}
	
	public void testUpdateProductInvalidPrice() {
		getDBCollection().save(new BasicDBObject("id", 1234)
		.append("title", "Sugar")
		.append("pricing", new BasicDBObject("cost", 10.5d).append("price",12d)));
		getDBCollection().save(new BasicDBObject("id", 1334));
		
		Product product = Product.findById(1234);
		product.pricing.price = 10.0d;
		
		try {
			product.updatePriceAndTitle();
		} catch(Exception e) {
			assertEquals("product.price.invalid", e.getMessage());
		}
		Product obj = Product.findById(1234);
		
		assertNotNull(obj);
		assertEquals(1234, obj.id.intValue());
		assertEquals("Sugar", obj.title);
		assertEquals(10.5d, obj.pricing.cost);
		assertEquals(12d, obj.pricing.price);
	}

}
