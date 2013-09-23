package com.wikishow.repository;

import com.mongodb.WriteConcern;
import com.wikishow.entity.EpisodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public void addEpisodeData(EpisodeEntity episodeEntity) {
        if (!mongoTemplate.collectionExists(EpisodeEntity.class)) {
            mongoTemplate.createCollection(EpisodeEntity.class);
        }
        mongoTemplate.insert(episodeEntity, COLLECTION_NAME);
    }

    public List<EpisodeEntity> listAllEpisodes() {
        return mongoTemplate.findAll(EpisodeEntity.class, COLLECTION_NAME);
    }

    public EpisodeEntity findById(String id) {
        return mongoTemplate.findById(id, EpisodeEntity.class, COLLECTION_NAME);
    }

    public void deleteEpisode(EpisodeEntity episodeEntity) {
        mongoTemplate.remove(episodeEntity, COLLECTION_NAME);
    }

    public void updateEpisode(String field, String value, String updateField, String updateValue) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        query.fields().include(field);
        Update update = new Update();
        update.set(updateField, updateValue);
        mongoTemplate.setWriteConcern(WriteConcern.ERRORS_IGNORED);
        mongoTemplate.updateFirst(query, update, EpisodeEntity.class, COLLECTION_NAME);
    }
}
