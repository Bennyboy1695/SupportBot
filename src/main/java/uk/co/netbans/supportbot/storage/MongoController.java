package uk.co.netbans.supportbot.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoController {
    private final MongoClient client;
    private final MongoDatabase main;

    public MongoController(String hostname, int port, String database, String username, String password, String authDb) {
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);

        client = new MongoClient(new MongoClientURI(String.format(
                "mongodb://%s:%s@%s:%s/%s?retryWrites=true",
                username, password, hostname, port, authDb)));
        main = client.getDatabase(database);
    }

    public void shutdown() {
        client.close();
    }

    public boolean collectionExists(String name) {
        for (String coll : main.listCollectionNames())
            if (name.equals(coll))
                return true;
        return false;
    }

    public MongoCollection<Document> getCollection(String name) {
        if (!collectionExists(name))
            main.createCollection(name);
        return main.getCollection(name);
    }

    public MongoDatabase getDatabase() {
        return main;
    }

    public MongoClient getClient() {
        return client;
    }
}
