package com.wikishow.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.CastAndCrew;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/22/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CastRepository extends DefaultRepository {

    public void addCastData(CastAndCrew castEntity) {
        getMapper();
        if (castEntity.getName() == null || castEntity.getName().isEmpty()) {
            return;
        }
        mapper.save(castEntity);
    }

    public void deleteCast(CastAndCrew castEntity) {
        getMapper();
        mapper.delete(castEntity);
    }

    public List<CastAndCrew> findByIds(Set<String> castNames) {
        getMapper();
        if (castNames == null || castNames.isEmpty()) {
            return null;
        }

        System.out.println("Starting CastRepository.findByIds size=" + castNames.size());
        List<CastAndCrew> scanResult = new ArrayList<CastAndCrew>();

        for (String castName : castNames) {
            scanResult.add(findByName(castName));
        }
        System.out.println("Finished CastRepository.findByIds size=" + scanResult.size());
        return scanResult;
    }

    public CastAndCrew findByName(String name) {
        getMapper();
        System.out.println("Starting CastRepository.findByName name=" + name);
        if (name == null || name.isEmpty()) {
            return null;
        }
        return mapper.load(CastAndCrew.class, name);
    }
}
