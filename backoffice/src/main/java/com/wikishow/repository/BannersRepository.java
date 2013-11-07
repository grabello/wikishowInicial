package com.wikishow.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.Banners;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/1/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BannersRepository extends DefaultRepository {

    public void addBanner(Banners banners) {
        getMapper();
        mapper.save(banners);
    }

    public Banners findBySeriesId(String seriesId) {
        getMapper();
        return mapper.load(Banners.class, seriesId);
    }

    public Banners findByIdAndSeriesId(String id, String seriesId) {
        getMapper();
        Banners bannersEntity = new Banners();
        bannersEntity.setSeriesId(seriesId);
        Condition rangeKeyCondition = new Condition();
        rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(id));
        DynamoDBQueryExpression<Banners> queryExpression = new DynamoDBQueryExpression<Banners>()
                .withHashKeyValues(bannersEntity)
                .withRangeKeyCondition("Id", rangeKeyCondition);

        List<Banners> bannersList = mapper.query(Banners.class, queryExpression);

        if (bannersList == null || bannersList.isEmpty()) {
            return null;
        }

        return bannersList.get(0);
    }

    public List<Banners> findBySeriesIDAndType(String id, String type) {
        getMapper();
        Banners bannersEntity = new Banners();
        bannersEntity.setSeriesId(id);
        Condition rangeKeyCondition = new Condition();
        rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(type));
        DynamoDBQueryExpression<Banners> queryExpression = new DynamoDBQueryExpression<Banners>()
                .withHashKeyValues(bannersEntity)
                .withRangeKeyCondition("Type", rangeKeyCondition);

        List<Banners> bannersList = mapper.query(Banners.class, queryExpression);

        if (bannersList == null || bannersList.isEmpty()) {
            return null;
        }

        return bannersList;
    }

}
