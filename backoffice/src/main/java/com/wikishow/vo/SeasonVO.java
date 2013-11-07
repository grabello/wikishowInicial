package com.wikishow.vo;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SeasonVO {

    private String id;
    private Integer seasonNumber;
    private Date dateFirst;
    private Date dateLast;
    private List<EpisodeVO> episodes;
    private List<String> seasonURL;
    private String chooseSeasonURL;
    private List<String> seasonWideURL;
    private String chooseSeasonWideURL;
    private SortedMap<Integer, EpisodeVO> episodeMap;

    public SeasonVO(Integer seasonNumber, Date dateFirst, Date dateLast, List<String> seasonURL, List<String> seasonWideURL, SortedMap<Integer, EpisodeVO> episodeMap) {
        this.seasonNumber = seasonNumber;
        this.dateFirst = dateFirst;
        this.dateLast = dateLast;
        this.seasonURL = seasonURL;
        this.episodeMap = episodeMap;
        this.seasonWideURL = seasonWideURL;
    }

    public SeasonVO(String id, Integer seasonNumber, List<String> seasonURL, List<String> seasonWideURL) {
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.seasonURL = seasonURL;
        this.seasonWideURL = seasonWideURL;
    }

    public SeasonVO(Integer seasonNumber, Date dateFirst, Date dateLast, List<EpisodeVO> episodes, List<String> seasonURL, List<String> seasonWideURL) {
        this.seasonNumber = seasonNumber;
        this.dateFirst = dateFirst;
        this.dateLast = dateLast;
        this.episodes = episodes;
        this.seasonURL = seasonURL;
        this.seasonWideURL = seasonWideURL;
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

    public List<EpisodeVO> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<EpisodeVO> episodes) {
        this.episodes = episodes;
    }

    public List<String> getSeasonURL() {
        return seasonURL;
    }

    public void setSeasonURL(List<String> seasonURL) {
        this.seasonURL = seasonURL;
    }

    public SortedMap<Integer, EpisodeVO> getEpisodeMap() {
        return episodeMap;
    }

    public void setEpisodeMap(SortedMap<Integer, EpisodeVO> episodeMap) {
        this.episodeMap = episodeMap;
    }

    public List<String> getSeasonWideURL() {
        return seasonWideURL;
    }

    public void setSeasonWideURL(List<String> seasonWideURL) {
        this.seasonWideURL = seasonWideURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChooseSeasonURL() {
        return chooseSeasonURL;
    }

    public void setChooseSeasonURL(String chooseSeasonURL) {
        this.chooseSeasonURL = chooseSeasonURL;
    }

    public String getChooseSeasonWideURL() {
        return chooseSeasonWideURL;
    }

    public void setChooseSeasonWideURL(String chooseSeasonWideURL) {
        this.chooseSeasonWideURL = chooseSeasonWideURL;
    }

    @Override
    public String toString() {
        return "SeasonVO{" +
                "seasonNumber=" + seasonNumber +
                ", dateFirst=" + dateFirst +
                ", dateLast=" + dateLast +
                ", episodes=" + episodes +
                ", seasonURL=" + seasonURL +
                ", seasonWideURL=" + seasonWideURL +
                ", episodeMap=" + episodeMap +
                '}';
    }

}
