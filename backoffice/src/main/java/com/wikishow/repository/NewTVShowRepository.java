package com.wikishow.repository;

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
 * Date: 9/23/13
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class NewTVShowRepository {
    public static final String COLLECTION_NAME = "newTVShow";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addNewTVShowData(NewTVShow newTVShowEntity) {
        if (!mongoTemplate.collectionExists(NewTVShow.class)) {
            mongoTemplate.createCollection(NewTVShow.class);
        }
        mongoTemplate.insert(newTVShowEntity, COLLECTION_NAME);
    }

    public List<NewTVShow> listAllNewTVShow() {
        return mongoTemplate.findAll(NewTVShow.class, COLLECTION_NAME);
    }

    public NewTVShow findById(BigInteger id) {
        return mongoTemplate.findById(id, NewTVShow.class, COLLECTION_NAME);
    }

    public List<NewTVShow> findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));

        return mongoTemplate.find(query, NewTVShow.class, COLLECTION_NAME);
    }

    public void deleteNewTVShow(NewTVShow newTVShowEntity) {
        mongoTemplate.remove(newTVShowEntity, COLLECTION_NAME);
    }

}
