package com.wikishow.vo;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class TVShowVO {

    private String tvShowName;
    private String network;
    private Date firstAired;
    private String overview;
    private Boolean isEnded;
    private List<SeasonVO> seasons;
    private List<RoleVO> cast;
    private String airsTime;
    private Set<String> genre;
    //In Minutes
    private Integer runTime;
    private List<String> banners;
    private List<String> series;
    private SortedMap<Integer, String> seasonMap;

    public TVShowVO(String tvShowName, String network, Date firstAired, String overview, Boolean ended, List<SeasonVO> seasons, List<RoleVO> cast, String airsTime, Set<String> genre, Integer runTime, List<String> banners, List<String> series) {
        this.tvShowName = tvShowName;
        this.network = network;
        this.firstAired = firstAired;
        this.overview = overview;
        isEnded = ended;
        this.seasons = seasons;
        this.cast = cast;
        this.airsTime = airsTime;
        this.genre = genre;
        this.runTime = runTime;
        this.banners = banners;
        this.series = series;
    }

    public TVShowVO(String tvShowName, String network, Date firstAired, String overview, Boolean ended, List<RoleVO> cast, String airsTime, Set<String> genre, Integer runTime, List<String> banners, List<String> series, SortedMap<Integer, String> seasonMap) {
        this.tvShowName = tvShowName;
        this.network = network;
        this.firstAired = firstAired;
        this.overview = overview;
        isEnded = ended;
        this.cast = cast;
        this.airsTime = airsTime;
        this.genre = genre;
        this.runTime = runTime;
        this.banners = banners;
        this.series = series;
        this.seasonMap = seasonMap;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Boolean getEnded() {
        return isEnded;
    }

    public void setEnded(Boolean ended) {
        isEnded = ended;
    }

    public List<SeasonVO> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<SeasonVO> seasons) {
        this.seasons = seasons;
    }

    public List<RoleVO> getCast() {
        return cast;
    }

    public void setCast(List<RoleVO> cast) {
        this.cast = cast;
    }

    public String getAirsTime() {
        return airsTime;
    }

    public void setAirsTime(String airsTime) {
        this.airsTime = airsTime;
    }

    public Set<String> getGenre() {
        return genre;
    }

    public void setGenre(Set<String> genre) {
        this.genre = genre;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public List<String> getBanners() {
        return banners;
    }

    public void setBanners(List<String> banners) {
        this.banners = banners;
    }

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }

    public SortedMap<Integer, String> getSeasonMap() {
        return seasonMap;
    }

    public void setSeasonMap(SortedMap<Integer, String> seasonMap) {
        this.seasonMap = seasonMap;
    }

    @Override
    public String toString() {
        return "TVShowVO{" +
                "tvShowName='" + tvShowName + '\'' +
                ", network='" + network + '\'' +
                ", firstAired=" + firstAired +
                ", overview='" + overview + '\'' +
                ", isEnded=" + isEnded +
                ", seasons=" + seasons +
                ", cast=" + cast +
                ", airsTime='" + airsTime + '\'' +
                ", genre=" + genre +
                ", runTime=" + runTime +
                ", banners=" + banners +
                ", series=" + series +
                ", seasonMap=" + seasonMap +
                '}';
    }
}
