package com.wikishow.repository;

import com.wikishow.entity.Banners;
import com.wikishow.entity.NewTVShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/1/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BannersRepository {

    public static final String COLLECTION_NAME = "banners";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addBanner(Banners banners) {
        if (!mongoTemplate.collectionExists(Banners.class)) {
            mongoTemplate.createCollection(Banners.class);
        }
        mongoTemplate.insert(banners, COLLECTION_NAME);
    }

    public Banners findById(String id) {
        return mongoTemplate.findById(id, Banners.class, COLLECTION_NAME);
    }


}
