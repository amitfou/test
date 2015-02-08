package util;
import play.Play;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;


public class MongoDBUtil {
	
	private static MongoClient mongoClient;
	private static DB db;
	private static DBCollection coll;

	private MongoDBUtil(){}

	public static void init() {
		try {
			
			mongoClient = new MongoClient(Play.application().configuration().getString("mongodb.host", "localhost"), 
					Play.application().configuration().getInt("mongodb.port", 27017));
			db = mongoClient.getDB(Play.application().configuration().getString("mongodb.db", "test"));
			coll = db.getCollection(Play.application().configuration().getString("mongodb.collection", "collection"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DBCollection getColl() {
		return coll;
	}
	
	public static void setColl(DBCollection coll) {
		MongoDBUtil.coll = coll;
	}
	
	public static void close() {
		mongoClient.close();
	}
}
