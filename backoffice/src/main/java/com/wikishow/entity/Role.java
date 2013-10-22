package com.wikishow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/2/13
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "Role")
public class Role {

    private String id;
    private String role;
    private String castName;
    private String tvShowId;
    private String url;

    @DynamoDBAttribute(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBRangeKey(attributeName = "CastName")
    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    @DynamoDBHashKey(attributeName = "Role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @DynamoDBIndexRangeKey(attributeName = "TVShowID", localSecondaryIndexName = "TVShowIDIndex")
    public String getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(String tvShowId) {
        this.tvShowId = tvShowId;
    }

    @DynamoDBAttribute(attributeName = "URL")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
