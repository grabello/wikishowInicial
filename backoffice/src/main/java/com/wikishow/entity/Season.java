package com.wikishow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/22/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "Season")
public class Season {

    private String id;
    private Integer seasonNumber;
    private Date dateFirst;
    private Date dateLast;
    private String seriesID;
    private Set<String> episodes;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "SeasonNumber")
    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    @DynamoDBAttribute(attributeName = "DateBegin")
    public Date getDateFirst() {
        return dateFirst;
    }

    public void setDateFirst(Date dateFirst) {
        this.dateFirst = dateFirst;
    }

    @DynamoDBAttribute(attributeName = "DateEnd")
    public Date getDateLast() {
        return dateLast;
    }

    public void setDateLast(Date dateLast) {
        this.dateLast = dateLast;
    }

    @DynamoDBAttribute(attributeName = "Episodes")
    public Set<String> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Set<String> episodes) {
        this.episodes = episodes;
    }

    @DynamoDBIndexRangeKey(attributeName = "SeriesID")
    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }
}
