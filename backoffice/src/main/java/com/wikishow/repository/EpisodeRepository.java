package com.wikishow.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.Episode;
import org.springframework.stereotype.Repository;

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
public class EpisodeRepository extends DefaultRepository {

    public void addEpisodeData(Episode episodeEntity) {
        getMapper();
        mapper.save(episodeEntity);
    }

    public Episode findById(String id) {
        getMapper();
        System.out.println("Starting EpisodeRepository.findById id=" + id);
        return mapper.load(Episode.class, id);
    }

    public List<Episode> findByIds(Set<String> seasonIds){
        getMapper();
        System.out.println("Starting EpisodeRepository.findByIds size=" + seasonIds.size());
        List<Episode> scanResult;
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        Set<AttributeValue> attributeValueSet = new HashSet<AttributeValue>();
        for (String seasonId : seasonIds) {
            attributeValueSet.add(new AttributeValue().withS(seasonId));
        }
        scanExpression.addFilterCondition("Id", new Condition()
                .withComparisonOperator(ComparisonOperator.IN)
                .withAttributeValueList(attributeValueSet));

        scanResult = mapper.scan(Episode.class, scanExpression);
        System.out.println("Finished EpisodeRepository.findByIds size=" + scanResult.size());
        return scanResult;
    }

    public void deleteEpisode(Episode episodeEntity) {
        getMapper();
        mapper.delete(episodeEntity);
    }
}
