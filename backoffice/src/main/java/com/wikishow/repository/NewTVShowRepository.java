package com.wikishow.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.NewTVShow;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/23/13
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class NewTVShowRepository extends DefaultRepository {

    public void addNewTVShowData(NewTVShow newTVShowEntity) {
        if (findByName(newTVShowEntity.getName()) == null) {
            mapper.save(newTVShowEntity);
        }
    }

    public List<NewTVShow> listAllNewTVShow() {
        getMapper();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("Name",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.NOT_NULL));
        List<NewTVShow> scanResult = mapper.scan(NewTVShow.class, scanExpression);

        if (scanResult == null || scanResult.size() == 0) {
            return null;
        }

        if (scanResult.size() > 30) {
            return scanResult.subList(0, 30);
        }
        return scanResult;

    }

    public NewTVShow findByName(String id) {
        getMapper();
        return mapper.load(NewTVShow.class, id);
    }

    public void deleteNewTVShow(NewTVShow newTVShowEntity) {
        getMapper();
        mapper.delete(newTVShowEntity);
    }

    public int countNewTVShow() {
        getMapper();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("Name",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.NOT_NULL));
        return mapper.count(NewTVShow.class, scanExpression);
    }

}
