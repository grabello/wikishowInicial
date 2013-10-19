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
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "Episode")
public class Episode {

    private String id;
    private String name_pt;
    private String name_en;
    private Integer episodeNumber;
    private String seasonID;
    private String seriesID;
    private Integer seasonNumber;
    private String overview_en;
    private String overview_pt;
    private Set<String> guestStars;
    private Set<String> directors;
    private Set<String> writers;
    private Date firstAired;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBIndexRangeKey(attributeName = "NamePT", localSecondaryIndexName = "NamePTIndex")
    public String getName_pt() {
        return name_pt;
    }

    public void setName_pt(String name_pt) {
        this.name_pt = name_pt;
    }

    @DynamoDBIndexRangeKey(attributeName = "NameEN", localSecondaryIndexName = "NameENIndex")
    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    @DynamoDBAttribute(attributeName = "EpisodeNumber")
    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    @DynamoDBAttribute(attributeName = "SeasonNumber")
    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    @DynamoDBAttribute(attributeName = "SeasonID")
    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    @DynamoDBAttribute(attributeName = "SeriesID")
    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
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

    @DynamoDBAttribute(attributeName = "GuestStars")
    public Set<String> getGuestStars() {
        return guestStars;
    }

    public void setGuestStars(Set<String> guestStars) {
        this.guestStars = guestStars;
    }

    @DynamoDBAttribute(attributeName = "Directors")
    public Set<String> getDirectors() {
        return directors;
    }

    public void setDirectors(Set<String> directors) {
        this.directors = directors;
    }

    @DynamoDBAttribute(attributeName = "Writers")
    public Set<String> getWriters() {
        return writers;
    }

    public void setWriters(Set<String> writers) {
        this.writers = writers;
    }

    @DynamoDBAttribute(attributeName = "FirstAired")
    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(Date firstAired) {
        this.firstAired = firstAired;
    }
}
