package com.wikishow.vo;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeVO {

    private String id;
    private String name;
    private Integer episodeNumber;
    private String overview;
    private List<CastAndCrewVO> guestStars;
    private List<CastAndCrewVO> directors;
    private List<CastAndCrewVO> writers;
    private Date firstAired;
    private String episodeImage;

    public EpisodeVO(String id, String name, Integer episodeNumber, Date firstAired, String episodeImage) {
        this.id = id;
        this.name = name;
        this.episodeNumber = episodeNumber;
        this.firstAired = firstAired;
        this.episodeImage = episodeImage;
    }

    public EpisodeVO(String name, Integer episodeNumber, String overview, List<CastAndCrewVO> guestStars, List<CastAndCrewVO> directors, List<CastAndCrewVO> writers, Date firstAired, String episodeImage) {
        this.name = name;
        this.episodeNumber = episodeNumber;
        this.overview = overview;
        this.guestStars = guestStars;
        this.directors = directors;
        this.writers = writers;
        this.firstAired = firstAired;
        this.episodeImage = episodeImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<CastAndCrewVO> getGuestStars() {
        return guestStars;
    }

    public void setGuestStars(List<CastAndCrewVO> guestStars) {
        this.guestStars = guestStars;
    }

    public List<CastAndCrewVO> getDirectors() {
        return directors;
    }

    public void setDirectors(List<CastAndCrewVO> directors) {
        this.directors = directors;
    }

    public List<CastAndCrewVO> getWriters() {
        return writers;
    }

    public void setWriters(List<CastAndCrewVO> writers) {
        this.writers = writers;
    }

    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(Date firstAired) {
        this.firstAired = firstAired;
    }

    public String getEpisodeImage() {
        return episodeImage;
    }

    public void setEpisodeImage(String episodeImage) {
        this.episodeImage = episodeImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EpisodeVO{" +
                "name='" + name + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", overview='" + overview + '\'' +
                ", guestStars=" + guestStars +
                ", directors=" + directors +
                ", writers=" + writers +
                ", firstAired=" + firstAired +
                ", episodeImage='" + episodeImage + '\'' +
                '}';
    }
}
