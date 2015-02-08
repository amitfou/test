

import play.Application;
import play.GlobalSettings;
import play.Logger;
import util.MongoDBUtil;


public class Global extends GlobalSettings {
	
	@Override
	  public void onStart(Application app) { 
		  MongoDBUtil.init();
		  Logger.info("Application has started");
	  }  
	  
		@Override
	  public void onStop(Application app) {
		MongoDBUtil.close();
	    Logger.info("Application shutdown...");
	  }  
	    
	}

