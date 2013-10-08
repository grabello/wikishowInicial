package com.wikishow.repository;

import com.mongodb.WriteConcern;
import com.wikishow.entity.TvShow;
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

    public static final String COLLECTION_NAME = "tvShow";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addTVShowData(TvShow tvShowEntity) {
        if (!mongoTemplate.collectionExists(TvShow.class)) {
            mongoTemplate.createCollection(TvShow.class);
        }
        mongoTemplate.insert(tvShowEntity, COLLECTION_NAME);
    }

    public List<TvShow> listAllEpisodes() {
        return mongoTemplate.findAll(TvShow.class, COLLECTION_NAME);
    }

    public TvShow findById(String id) {
        if (id == null) {
            return null;
        }
        return mongoTemplate.findById(id, TvShow.class, COLLECTION_NAME);
    }

    public void deleteTVShow(TvShow TvShow) {
        mongoTemplate.remove(TvShow, COLLECTION_NAME);
    }

    public void updateTVShow(String field, String value, Map<String, Object> updateMap) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        query.fields().include(field);
        System.out.println("Updating TVShow " + field + "=" + value);
        Update update = new Update();
        if (updateMap != null) {
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
            mongoTemplate.updateFirst(query, update, TvShow.class, COLLECTION_NAME);
        } catch (ClassCastException e) {
            System.err.println("Failed to update tvshow " + field + "=" + value);

        }
    }
}
