package models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Logger.ALogger;
import play.i18n.Messages;
import util.MongoDBUtil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Product {
	private static final ALogger LOGGER = Logger.of(Product.class);
	
	private static ObjectMapper mapper;
	static {
	    mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public Integer id;
	public String title;
	public Pricing pricing;


	public static List<Integer> getProductIds(Integer productId) {
		LOGGER.info("Query for all product Ids that starts with: {}" , productId);
		BasicDBObject query = new BasicDBObject("$where", "/^" + productId+ ".*/.test(this.id)");
		BasicDBObject fields = new BasicDBObject("id", 1).append("_id", 0);
		List<Integer> ids = new ArrayList<Integer>();

		try (DBCursor cursor = MongoDBUtil.getColl().find(query, fields)){
			while (cursor.hasNext()) {
				DBObject id = cursor.next();
				LOGGER.debug("Got id: {}" , id);
				ids.add(Integer.parseInt("" + id.get("id")));
			}
		} 
		return ids;
	}

	public static Product findById(Integer productId) {
		Preconditions.checkState(productId != null, Messages.get("product.id.blank"));
		Product product = null;
		try(DBCursor cursor = MongoDBUtil.getColl().find(new BasicDBObject("id", productId))) {
			if (cursor.hasNext()) {
				product = mapper.convertValue(cursor.next(), Product.class);
			}
		} catch (Exception e) {
			LOGGER.error("Could not fetch the product details" + productId, e);
			throw new RuntimeException(Messages.get("system.db.error"));
		}
		return product;
	}

	public void updatePriceAndTitle() {
		Preconditions.checkState(id != null, Messages.get("product.id.blank"));
		Preconditions.checkState(StringUtils.isNotBlank(title), Messages.get("product.title.blank"));
		Preconditions.checkState(pricing.price != null, Messages.get("product.price.blank"));
		Preconditions.checkState(pricing.price > pricing.cost , Messages.get("product.price.invalid"));
		
		try {
			MongoDBUtil.getColl().update(
					new BasicDBObject("id", id),
					new BasicDBObject().append("$set",
					new BasicDBObject().append("title", title)
					.append("pricing.price", pricing.price)));
		} catch (Exception e) {
			LOGGER.error("Could not update the product details" + id, e);
			throw new RuntimeException(Messages.get("system.db.error"));
		}

	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", title=" + title + ", pricing="
				+ pricing + "]";
	}
	
	
	
	
}
