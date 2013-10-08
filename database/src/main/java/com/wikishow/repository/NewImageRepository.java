package com.wikishow.repository;

import com.wikishow.entity.NewImage;
import com.wikishow.entity.NewTVShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/4/13
 * Time: 8:15 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class NewImageRepository {

    public static final String COLLECTION_NAME = "tvShow";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addNewImage(NewImage newImageEntity) {
        if (!mongoTemplate.collectionExists(NewImage.class)) {
            mongoTemplate.createCollection(NewImage.class);
        }
        mongoTemplate.insert(newImageEntity, COLLECTION_NAME);
    }

    public List<NewImage> listAllImages() {
        return mongoTemplate.findAll(NewImage.class, COLLECTION_NAME);
    }

    public void deleteNewImage(NewImage newImageEntity) {
        mongoTemplate.remove(newImageEntity, COLLECTION_NAME);
    }

}
