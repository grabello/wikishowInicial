package com.wikishow.repository;

import com.mongodb.WriteConcern;
import com.wikishow.entity.Episode;
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
public class EpisodeRepository {

    public static final String COLLECTION_NAME = "episode";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addEpisodeData(Episode episodeEntity) {
        if (!mongoTemplate.collectionExists(Episode.class)) {
            mongoTemplate.createCollection(Episode.class);
        }
        mongoTemplate.insert(episodeEntity, COLLECTION_NAME);
    }

    public List<Episode> listAllEpisodes() {
        return mongoTemplate.findAll(Episode.class, COLLECTION_NAME);
    }

    public Episode findById(String id) {
        return mongoTemplate.findById(id, Episode.class, COLLECTION_NAME);
    }

    public void deleteEpisode(Episode episodeEntity) {
        mongoTemplate.remove(episodeEntity, COLLECTION_NAME);
    }

    public void updateEpisode(String field, String value, Map<String, Object> updateMap) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        query.fields().include(field);
        Update update = new Update();
        System.out.println("Updating Episode " + field + "=" + value);
        if (updateMap != null && updateMap.size() > 0) {
            Set<String> fields = updateMap.keySet();
            if (fields != null && !fields.isEmpty()) {
                for (String updateField : fields) {
                    System.out.println(updateField + "=" + updateMap.get(updateField));
                    update.set(updateField, updateMap.get(updateField));
                }
            } else {
                return;
            }
        } else {
            return;
        }
        mongoTemplate.setWriteConcern(WriteConcern.ERRORS_IGNORED);
        try {
            mongoTemplate.updateFirst(query, update, Episode.class, COLLECTION_NAME);
        } catch (ClassCastException e) {
            System.err.println("Failed to update episode " + field + "=" + value);
        }
    }
}
