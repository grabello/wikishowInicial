package com.wikishow.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.TvShow;
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
public class TVShowRepository extends DefaultRepository {

    public void addTVShowData(TvShow tvShowEntity) {
        getMapper();
        mapper.save(tvShowEntity);
    }

    public TvShow findById(String id) {
        getMapper();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("Id",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue().withS(id)));
        List<TvShow> scanResult = mapper.scan(TvShow.class, scanExpression);

        if (scanResult == null || scanResult.size() == 0) {
            return null;
        }
        return scanResult.get(0);
    }

    public TvShow findByTVShowName(String tvShowName) {
        getMapper();
        if (tvShowName == null) {
            return null;
        }
        return mapper.load(TvShow.class, tvShowName);
    }

    public int countTVShow() {
        getMapper();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("TVShowName",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.NOT_NULL));
         return mapper.count(TvShow.class, scanExpression);
    }

    public void deleteTVShow(TvShow tvShow) {
        getMapper();
        mapper.delete(tvShow);
    }

}
