package com.wikishow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/18/13
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "TVDBData")
public class TVDBData {

    private String id;
    private String lastUpdateTime;
    private String mirror;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "LastUpdateTime")
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @DynamoDBAttribute(attributeName = "Mirror")
    public String getMirror() {
        return mirror;
    }

    public void setMirror(String mirror) {
        this.mirror = mirror;
    }
}
