package com.wikishow.repository;

import com.mongodb.WriteConcern;
import com.wikishow.entity.Season;
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
public class SeasonRepository {

    public static final String COLLECTION_NAME = "season";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addSeasonData(Season seasonEntity) {
        if (!mongoTemplate.collectionExists(Season.class)) {
            mongoTemplate.createCollection(Season.class);
        }
        mongoTemplate.insert(seasonEntity, COLLECTION_NAME);
    }

    public List<Season> listAllEpisodes() {
        return mongoTemplate.findAll(Season.class, COLLECTION_NAME);
    }

    public Season findById(String id) {
        return mongoTemplate.findById(id, Season.class, COLLECTION_NAME);
    }

    public void deleteSeason(Season Season) {
        mongoTemplate.remove(Season, COLLECTION_NAME);
    }

    public void updateSeason(String field, String value, Map<String, Object> updateMap) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        query.fields().include(field);
        Update update = new Update();
        System.out.println("Updating Season " + field + "=" + value);
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
            mongoTemplate.updateFirst(query, update, Season.class, COLLECTION_NAME);
        } catch (ClassCastException e) {
            System.err.println("Failed to update season " + field + "=" + value);

        }
    }
}
