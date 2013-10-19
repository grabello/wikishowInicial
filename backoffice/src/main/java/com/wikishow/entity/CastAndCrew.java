package com.wikishow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/22/13
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "CastAndCrew")
public class CastAndCrew {

    private String name;
    private String type;

    @DynamoDBHashKey(attributeName = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
