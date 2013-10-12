package com.wikishow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.S3Link;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/1/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "Banners")
public class Banners {

    private String id;
    private String seriesId;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "SeriesID")
    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }
}
