package com.wikishow.entity;

import com.wikishow.repository.EpisodeRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/22/13
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
@Document
public class Episode {

    @Id
    private String id;
    @Indexed
    private String name_pt;
    @Indexed
    private String name_en;
    private Integer episodeNumber;
    private Integer seasonNumber;
    private String overview_en;
    private String overview_pt;
    @DBRef
    private List<CastAndCrew> guestStars;
    @DBRef
    private List<CastAndCrew> directors;
    @DBRef
    private List<CastAndCrew> writers;
    private Date firstAired;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_pt() {
        return name_pt;
    }

    public void setName_pt(String name_pt) {
        this.name_pt = name_pt;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getOverview_en() {
        return overview_en;
    }

    public void setOverview_en(String overview_en) {
        this.overview_en = overview_en;
    }

    public String getOverview_pt() {
        return overview_pt;
    }

    public void setOverview_pt(String overview_pt) {
        this.overview_pt = overview_pt;
    }

    public List<CastAndCrew> getGuestStars() {
        return guestStars;
    }

    public void setGuestStars(List<CastAndCrew> guestStars) {
        this.guestStars = guestStars;
    }

    public List<CastAndCrew> getDirectors() {
        return directors;
    }

    public void setDirectors(List<CastAndCrew> directors) {
        this.directors = directors;
    }

    public List<CastAndCrew> getWriters() {
        return writers;
    }

    public void setWriters(List<CastAndCrew> writers) {
        this.writers = writers;
    }

    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(Date firstAired) {
        this.firstAired = firstAired;
    }
}
