package com.wikishow.dao;

import com.wikishow.entity.TVDBData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    public void updateTVDBData(TVDBData tvdbData) {
        mongoTemplate.insert(tvdbData, COLLECTION_NAME);
    }
}
