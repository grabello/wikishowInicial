package com.wikishow.repository;

import com.mongodb.WriteConcern;
import com.wikishow.entity.TVShowEntity;
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
public class TVShowRepository {

    public static final String COLLECTION_NAME = "tvshow";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addTVShowData(TVShowEntity tvShowEntity) {
        if (!mongoTemplate.collectionExists(TVShowEntity.class)) {
            mongoTemplate.createCollection(TVShowEntity.class);
        }
        mongoTemplate.insert(tvShowEntity, COLLECTION_NAME);
    }

    public List<TVShowEntity> listAllEpisodes() {
        return mongoTemplate.findAll(TVShowEntity.class, COLLECTION_NAME);
    }

    public TVShowEntity findById(String id) {
        if (id == null) {
            return null;
        }
        return mongoTemplate.findById(id, TVShowEntity.class, COLLECTION_NAME);
    }

    public void deleteTVShow(TVShowEntity TVShowEntity) {
        mongoTemplate.remove(TVShowEntity, COLLECTION_NAME);
    }

    public void updateTVShow(String field, String value, Map<String, Object> updateMap) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        query.fields().include(field);
        Update update = new Update();
        if (updateMap != null) {
            Set<String> fields = updateMap.keySet();
            if (fields != null && !fields.isEmpty()) {
                for (String updateField : fields) {
                    update.set(updateField, updateMap.get(updateField));
                }
            } else {
                return;
            }
        } else {
            return;
        }
        mongoTemplate.setWriteConcern(WriteConcern.ERRORS_IGNORED);
        mongoTemplate.updateFirst(query, update, TVShowEntity.class, COLLECTION_NAME);
    }
}
