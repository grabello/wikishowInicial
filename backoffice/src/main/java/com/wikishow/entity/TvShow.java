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
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "TVShow")
public class TvShow {

    private String id;
    private String tvShowName;
    private String network;
    private Date firstAired;
    private String overview_en;
    private String overview_pt;
    private Boolean isEnded;
    private Set<String> seasons;
    private Set<String> cast;
    private String airsTime;
    private Set<String> genre;
    //In Minutes
    private Integer runTime;

    @DynamoDBAttribute(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBHashKey(attributeName = "TVShowName")
    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
    }

    @DynamoDBAttribute(attributeName = "Network")
    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @DynamoDBAttribute(attributeName = "FirstAired")
    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(Date firstAired) {
        this.firstAired = firstAired;
    }

    @DynamoDBAttribute(attributeName = "OverviewEN")
    public String getOverview_en() {
        return overview_en;
    }

    public void setOverview_en(String overview_en) {
        this.overview_en = overview_en;
    }

    @DynamoDBAttribute(attributeName = "OverviewPT")
    public String getOverview_pt() {
        return overview_pt;
    }

    public void setOverview_pt(String overview_pt) {
        this.overview_pt = overview_pt;
    }

    @DynamoDBAttribute(attributeName = "Ended")
    public Boolean getEnded() {
        return isEnded;
    }

    public void setEnded(Boolean ended) {
        isEnded = ended;
    }

    @DynamoDBAttribute(attributeName = "Seasons")
    public Set<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(Set<String> seasons) {
        this.seasons = seasons;
    }

    @DynamoDBAttribute(attributeName = "Cast")
    public Set<String> getCast() {
        return cast;
    }

    public void setCast(Set<String> cast) {
        this.cast = cast;
    }

    @DynamoDBAttribute(attributeName = "AirsTime")
    public String getAirsTime() {
        return airsTime;
    }

    public void setAirsTime(String airsTime) {
        this.airsTime = airsTime;
    }

    @DynamoDBAttribute(attributeName = "Genre")
    public Set<String> getGenre() {
        return genre;
    }

    public void setGenre(Set<String> genre) {
        this.genre = genre;
    }

    @DynamoDBAttribute(attributeName = "RunTime")
    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }
}
