package com.wikishow.repository;

import com.wikishow.entity.Role;
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
 * Date: 10/3/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class RoleRepository {

    public static final String COLLECTION_NAME = "role";
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addRole(Role role) {
        if (!mongoTemplate.collectionExists(Role.class)) {
            mongoTemplate.createCollection(Role.class);
        }
        mongoTemplate.insert(role, COLLECTION_NAME);
    }

    public List<Role> listRoleBySeriesId(String seriesId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tvShowId").is(seriesId));

        return mongoTemplate.find(query, Role.class, COLLECTION_NAME);
    }

    public Role findByRoleAndSeriesId(String role, String seriesId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("role").is(role));
        query.addCriteria(Criteria.where("tvShowId").is(seriesId));

        List<Role> roleList = mongoTemplate.find(query, Role.class, COLLECTION_NAME);
        return roleList != null && roleList.size() > 0 ? roleList.get(0) : null;
    }

    public Role findById(BigInteger id) {
        return mongoTemplate.findById(id, Role.class, COLLECTION_NAME);
    }

}
