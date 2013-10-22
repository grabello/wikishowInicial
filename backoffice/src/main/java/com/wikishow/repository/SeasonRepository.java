package com.wikishow.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.Season;
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
public class SeasonRepository extends DefaultRepository {

    public void addSeasonData(Season seasonEntity) {
        getMapper();
        mapper.save(seasonEntity);
    }

    public Season findById(String id) {
        getMapper();
        return mapper.load(Season.class, id);
    }

    public List<Season> findByIds(Set<String> seasonIds){
        getMapper();
        if (seasonIds == null || seasonIds.isEmpty()) {
            return null;
        }
        System.out.println("Starting SeasonRepository.findByIds size=" + seasonIds.size());
        List<Season> scanResult = new ArrayList<Season>();
        for (String seasonId : seasonIds) {
            scanResult.add(findById(seasonId));
        }
        System.out.println("Finished SeasonRepository.findByIds size=" + scanResult.size());
        return scanResult;
    }

    public void deleteSeason(Season season) {
        getMapper();
        mapper.delete(season);
    }

}
