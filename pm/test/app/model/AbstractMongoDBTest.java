package app.model;

import junit.framework.TestCase;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
 public abstract class AbstractMongoDBTest extends TestCase  {

        /**
         * please store Starter or RuntimeConfig in a static final field
         * if you want to use artifact store caching (or else disable caching)
         */
        private static final MongodStarter starter = MongodStarter.getDefaultInstance();

        private MongodExecutable _mongodExe;
        private MongodProcess _mongod;

        private MongoClient  _mongo;
    	private DB _db;
    	private DBCollection _coll;
        @Override
        protected void setUp() throws Exception {

            _mongodExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(12345, Network.localhostIsIPv6()))
                .build());
            _mongod = _mongodExe.start();

            super.setUp();

            _mongo = new MongoClient("localhost", 12345);
            _db = _mongo.getDB("test");
			_coll = _db.getCollection("collection");
        }

        @Override
        protected void tearDown() throws Exception {
            super.tearDown();

            _mongod.stop();
            _mongodExe.stop();
        }

        public Mongo getMongo() {
            return _mongo;
        }

        public DBCollection getDBCollection() {
        	return _coll;
        }
    }