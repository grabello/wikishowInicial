package com.wikishow.entity;

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
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
@Document
public class TVShowEntity {

    @Id
    private String id;
    @Indexed
    private String tvShowName;
    private String network;
    private Date firstAired;
    private String overview_en;
    private String overview_pt;
    private Boolean isEnded;
    @DBRef
    private List<SeasonEntity> seasons;
    @DBRef
    private List<CastEntity> cast;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(Date firstAired) {
        this.firstAired = firstAired;
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

    public Boolean getEnded() {
        return isEnded;
    }

    public void setEnded(Boolean ended) {
        isEnded = ended;
    }

    public List<SeasonEntity> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<SeasonEntity> seasons) {
        this.seasons = seasons;
    }

    public List<CastEntity> getCast() {
        return cast;
    }

    public void setCast(List<CastEntity> cast) {
        this.cast = cast;
    }
}
