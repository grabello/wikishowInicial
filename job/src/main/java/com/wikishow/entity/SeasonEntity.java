package com.wikishow.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/22/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Document
public class SeasonEntity {

    @Id
    private String id;
    private Integer seasonNumber;
    private Date dateFirst;
    private Date dateLast;
    @DBRef
    private List<EpisodeEntity> episodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Date getDateFirst() {
        return dateFirst;
    }

    public void setDateFirst(Date dateFirst) {
        this.dateFirst = dateFirst;
    }

    public Date getDateLast() {
        return dateLast;
    }

    public void setDateLast(Date dateLast) {
        this.dateLast = dateLast;
    }

    public List<EpisodeEntity> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<EpisodeEntity> episodes) {
        this.episodes = episodes;
    }
}
