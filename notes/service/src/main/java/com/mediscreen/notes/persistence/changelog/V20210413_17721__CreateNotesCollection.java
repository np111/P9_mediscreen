package com.mediscreen.notes.persistence.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

@ChangeLog(order = "20210413_17721")
public class V20210413_17721__CreateNotesCollection {
    @ChangeSet(order = "001", id = "createNotesCollection", author = "Nathan Poirier")
    public void createNotesCollection(MongockTemplate mongo) {
        MongoCollection<?> coll = mongo.createCollection("notes");
        coll.createIndex(
                new BsonDocument()
                        .append("patientId", new BsonInt32(1))
                        .append("content", new BsonString("text")),
                new IndexOptions()
                        .textVersion(3)
                        .defaultLanguage("none"));
    }
}
