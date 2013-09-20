package com.wikishow.dao;

import com.mongodb.WriteConcern;
import com.wikishow.entity.TVDBData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/18/13
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class TVDBDAOMongo {

    public static final String COLLECTION_NAME = "tvdbdata";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void addTVDBData(TVDBData tvdbData) {
        if (!mongoTemplate.collectionExists(TVDBData.class)) {
            mongoTemplate.createCollection(TVDBData.class);
        }
        tvdbData.setId("1");
        mongoTemplate.insert(tvdbData, COLLECTION_NAME);
    }

    public List<TVDBData> listTVDBData() {
        return mongoTemplate.findAll(TVDBData.class, COLLECTION_NAME);
    }

    public TVDBData findById() {
        return mongoTemplate.findById("1", TVDBData.class, COLLECTION_NAME);
    }

    public void deleteTVDBData(TVDBData tvdbData) {
        mongoTemplate.remove(tvdbData, COLLECTION_NAME);
    }

    public void updateTVDBData(String field, String value, String updateField, String updateValue) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        query.fields().include(field);
        Update update = new Update();
        update.set(updateField, updateValue);
        mongoTemplate.setWriteConcern(WriteConcern.ERRORS_IGNORED);

        mongoTemplate.updateFirst(query, update, TVDBData.class, COLLECTION_NAME);


    }
}