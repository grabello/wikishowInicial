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
    private String url;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null && !id.isEmpty()) {
            this.id = id;
        }
    }

    @DynamoDBIndexRangeKey(attributeName = "NamePT", localSecondaryIndexName = "NamePTIndex")
    public String getName_pt() {
        return name_pt;
    }

    public void setName_pt(String name_pt) {
        if (name_pt != null && !name_pt.isEmpty()) {
            this.name_pt = name_pt;
        }
    }

    @DynamoDBIndexRangeKey(attributeName = "NameEN", localSecondaryIndexName = "NameENIndex")
    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        if (name_en != null && !name_en.isEmpty()) {
            this.name_en = name_en;
        }
    }

    @DynamoDBAttribute(attributeName = "EpisodeNumber")
    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        if (episodeNumber != null) {
            this.episodeNumber = episodeNumber;
        }
    }

    @DynamoDBAttribute(attributeName = "SeasonNumber")
    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        if (seasonNumber != null) {
            this.seasonNumber = seasonNumber;
        }
    }

    @DynamoDBAttribute(attributeName = "SeasonID")
    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        if (seasonID != null && !seasonID.isEmpty()) {
            this.seasonID = seasonID;
        }
    }

    @DynamoDBAttribute(attributeName = "SeriesID")
    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        if (seriesID != null && !seriesID.isEmpty()) {
            this.seriesID = seriesID;
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

    @DynamoDBAttribute(attributeName = "GuestStars")
    public Set<String> getGuestStars() {
        return guestStars;
    }

    public void setGuestStars(Set<String> guestStars) {
        if (guestStars != null && !guestStars.isEmpty()) {
            this.guestStars = guestStars;
        }
    }

    @DynamoDBAttribute(attributeName = "Directors")
    public Set<String> getDirectors() {
        return directors;
    }

    public void setDirectors(Set<String> directors) {
        if (directors != null && !directors.isEmpty()) {
            this.directors = directors;
        }
    }

    @DynamoDBAttribute(attributeName = "Writers")
    public Set<String> getWriters() {
        return writers;
    }

    public void setWriters(Set<String> writers) {
        if (writers != null && !writers.isEmpty()) {
            this.writers = writers;
        }
    }

    @DynamoDBAttribute(attributeName = "FirstAired")
    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(Date firstAired) {
        if (firstAired != null && !firstAired.toString().isEmpty())
            this.firstAired = firstAired;
    }

    @DynamoDBAttribute(attributeName = "URL")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
