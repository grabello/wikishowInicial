package com.wikishow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/1/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "Banners")
public class Banners {

    private String seriesId;
    private String id;
    private String type;
    private String url;

    @DynamoDBHashKey(attributeName = "SeriesID")
    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    @DynamoDBRangeKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBIndexRangeKey(attributeName = "Type", localSecondaryIndexName = "TypeIndex")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @DynamoDBAttribute(attributeName = "URL")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
