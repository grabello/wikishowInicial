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
    private String tvShowNamePT;
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
        if (id != null && !id.isEmpty()) {
            this.id = id;
        }
    }

    @DynamoDBHashKey(attributeName = "TVShowName")
    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        if (tvShowName != null && !tvShowName.isEmpty()) {
            this.tvShowName = tvShowName;
        }
    }

    @DynamoDBAttribute(attributeName = "TVShowNamePT")
    public String getTvShowNamePT() {
        return tvShowNamePT;
    }

    public void setTvShowNamePT(String tvShowNamePT) {
        this.tvShowNamePT = tvShowNamePT;
    }

    @DynamoDBAttribute(attributeName = "Network")
    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        if (network != null && !network.isEmpty()) {
            this.network = network;
        }
    }

    @DynamoDBAttribute(attributeName = "FirstAired")
    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(Date firstAired) {
        if (firstAired != null && !firstAired.toString().isEmpty()) {
            this.firstAired = firstAired;
        }
    }

    @DynamoDBAttribute(attributeName = "OverviewEN")
    public String getOverview_en() {
        return overview_en;
    }

    public void setOverview_en(String overview_en) {
        if (overview_en != null && !overview_en.isEmpty()) {
            this.overview_en = overview_en;
        }
    }

    @DynamoDBAttribute(attributeName = "OverviewPT")
    public String getOverview_pt() {
        return overview_pt;
    }

    public void setOverview_pt(String overview_pt) {
        if (overview_pt != null && !overview_pt.isEmpty()) {
            this.overview_pt = overview_pt;
        }
    }

    @DynamoDBAttribute(attributeName = "Ended")
    public Boolean getEnded() {
        return isEnded;
    }

    public void setEnded(Boolean ended) {
        if (ended != null) {
            isEnded = ended;
        }
    }

    @DynamoDBAttribute(attributeName = "Seasons")
    public Set<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(Set<String> seasons) {
        if (seasons != null && !seasons.isEmpty()) {
            this.seasons = seasons;
        }
    }

    @DynamoDBAttribute(attributeName = "Cast")
    public Set<String> getCast() {
        return cast;
    }

    public void setCast(Set<String> cast) {
        if (cast != null && !cast.isEmpty()) {
            this.cast = cast;
        }
    }

    @DynamoDBAttribute(attributeName = "AirsTime")
    public String getAirsTime() {
        return airsTime;
    }

    public void setAirsTime(String airsTime) {
        if (airsTime != null && !airsTime.isEmpty()) {
            this.airsTime = airsTime;
        }
    }

    @DynamoDBAttribute(attributeName = "Genre")
    public Set<String> getGenre() {
        return genre;
    }

    public void setGenre(Set<String> genre) {
        if (genre != null && !genre.isEmpty()) {
            this.genre = genre;
        }
    }

    @DynamoDBAttribute(attributeName = "RunTime")
    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        if (runTime != null) {
            this.runTime = runTime;
        }
    }
}
