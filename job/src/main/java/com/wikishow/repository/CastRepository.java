package com.wikishow.repository;

import com.mongodb.WriteConcern;
import com.wikishow.entity.CastAndCrew;
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

    public static final String COLLECTION_NAME = "castAndCrew";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addCastData(CastAndCrew castEntity) {
        if (!mongoTemplate.collectionExists(CastAndCrew.class)) {
            mongoTemplate.createCollection(CastAndCrew.class);
        }
        mongoTemplate.insert(castEntity, COLLECTION_NAME);
    }

    public List<CastAndCrew> listAllCast() {
        return mongoTemplate.findAll(CastAndCrew.class, COLLECTION_NAME);
    }

    public CastAndCrew findById(String id) {
        return mongoTemplate.findById(id, CastAndCrew.class, COLLECTION_NAME);
    }

    public void deleteCast(CastAndCrew castEntity) {
        mongoTemplate.remove(castEntity, COLLECTION_NAME);
    }

    public CastAndCrew findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, CastAndCrew.class, COLLECTION_NAME);
    }

    public void updateCast(String field, String value, Map<String, Object> updateMap) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        query.fields().include(field);
        Update update = new Update();
        System.out.println("Updating CastAndCrew " + field + "=" + value);
        if (!updateMap.isEmpty()) {
            Set<String> fields = updateMap.keySet();
            for (String updateField : fields) {
                System.out.println(updateField + "=" + updateMap.get(updateField));
                update.set(updateField, updateMap.get(updateField));
            }
        } else {
            return;
        }
        mongoTemplate.setWriteConcern(WriteConcern.ERRORS_IGNORED);
        try {
            mongoTemplate.updateFirst(query, update, CastAndCrew.class, COLLECTION_NAME);
        } catch (ClassCastException e) {
            System.err.println("Failed to update cast and crew " + field + "=" + value);

        }
    }
}
