package com.wikishow.repository;

import com.mongodb.WriteConcern;
import com.wikishow.entity.CastEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/22/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CastRepository {

    public static final String COLLECTION_NAME = "casting";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addCastData(CastEntity castEntity) {
        if (!mongoTemplate.collectionExists(CastEntity.class)) {
            mongoTemplate.createCollection(CastEntity.class);
        }
        mongoTemplate.insert(castEntity, COLLECTION_NAME);
    }

    public List<CastEntity> listAllCast() {
        return mongoTemplate.findAll(CastEntity.class, COLLECTION_NAME);
    }

    public CastEntity findById(String id) {
        return mongoTemplate.findById(id, CastEntity.class, COLLECTION_NAME);
    }

    public void deleteCast(CastEntity castEntity) {
        mongoTemplate.remove(castEntity, COLLECTION_NAME);
    }

    public CastEntity findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, CastEntity.class, COLLECTION_NAME);
    }

    public void updateCast(String field, String value, Map<String, String> updateMap) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        query.fields().include(field);
        Update update = new Update();
        if (!updateMap.isEmpty()) {
            Set<String> fields = updateMap.keySet();
            for (String updateField : fields) {
                update.set(updateField, updateMap.get(updateField));
            }
        } else {
            return;
        }
        mongoTemplate.setWriteConcern(WriteConcern.ERRORS_IGNORED);
        mongoTemplate.updateFirst(query, update, CastEntity.class, COLLECTION_NAME);
    }
}
