package controllers;

import java.util.List;

import models.Message;
import models.Product;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Logger.ALogger;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;


public class ManageProduct extends Controller {

	private static final ALogger LOGGER = Logger.of(ManageProduct.class);

    public static Result getProduct(String productId) {
    	LOGGER.info("Get product for id {}", productId);
    	Message message = null;
    	if (StringUtils.isNumeric(productId)) {
    		try {
    			Product obj = Product.findById(Integer.parseInt(productId));
    			if (obj != null) {
    				return Results.ok(Json.toJson(obj));
    			}
    			message = new Message("", Messages.get("product.not.found", productId));
    		} catch (Exception e) {
    			LOGGER.error("Could not find the product", e);
    			message =  new Message("danger", e.getMessage());
    		}
    	} else {
    		message =  new Message("", Messages.get("product.id.invalid", productId));
    	}
    	return Results.ok(Json.toJson(message));
    }

	
    
    public static Result getProductIds(String productId) {
    	LOGGER.info("Get ids like {}", productId);
    	if (StringUtils.isNumeric(productId)) {
        	List<Integer> ids = Product.getProductIds(Integer.parseInt(productId));
            return Results.ok(Json.toJson(ids));	
    	}
    	return Results.ok(); 
    }

    
    public static Result update() {
    	Message message = null;
    	JsonNode json = request().body().asJson();
    	  if(json == null) {
    	    return badRequest("Expecting Json data");
    	  } else {
    		Integer id = json.findPath("id").asInt();
    	    LOGGER.info("Save product for id {}", id);
    	    try {
	    		Product product = Product.findById(id);
	    		Preconditions.checkNotNull(product, Messages.get("product.not.found", "" + id));
	    		
	    		String title = json.findPath("title").textValue();
	    	    Double price = json.findPath("pricing").findPath("price").asDouble();
	    	    
	    	    product.title = title;
	    	    product.pricing.price = price;

	    	    product.updatePriceAndTitle();
    	    	message =  new Message("success", Messages.get("product.update.success", "" + id));
    	    } catch (Exception e) {
    	    	LOGGER.error("Could not save the product", e);
    	    	message =  new Message("danger", e.getMessage());
    	    }
    	    return ok((Json.toJson(message)));
    	  }
    }
    
	

}
