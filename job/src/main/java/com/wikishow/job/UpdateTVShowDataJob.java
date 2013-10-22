package com.wikishow.job;

import com.wikishow.entity.*;
import com.wikishow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/26/13
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
@EnableScheduling
public class UpdateTVShowDataJob {

    public static final String GET_UPDATE_URL = "http://thetvdb.com/api/Updates.php?type=all&time=";
    public static final String TVDB_API_KEY = "57550B35915D895C";
    public static final String GET_MIRROR_URL = "http://thetvdb.com/api/" + TVDB_API_KEY + "/mirrors.xml";
    public static final String GET_SERIES_UPDATE = "%xmlmirror%/api/" + TVDB_API_KEY + "/series/%seriesid%/all/%language%.xml";
    public static final String GET_EPISODES_UPDATE = "%xmlmirror%/api/" + TVDB_API_KEY + "/episodes/%episodesid%/%language%.xml";
    public static final String GET_BANNER_UPDATE = "%xmlmirror%/api/" + TVDB_API_KEY + "/series/%seriesid%/banners.xml";
    public static final String GET_ACTORS_UPDATE = "%xmlmirror%/api/" + TVDB_API_KEY + "/series/%seriesid%/actors.xml";
    public static final int XML_FILE = 1;
    public static final int BANNER_FILE = 2;
    @Autowired
    TVDBRepository tvDBRepository;
    @Autowired
    GetTVShowDataJob getTVShowDataJob;
    @Autowired
    TVShowRepository tvShowRepository;
    @Autowired
    NewTVShowRepository newTVShowRepository;
    @Autowired
    EpisodeRepository episodeRepository;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    private MailSender mailSender;
    private String xmlMirror = null;
    private String bannerMirror = null;
    private String timeUpdate = null;

//    @Scheduled(cron = "* 30 */2 * * *")
    @Scheduled(cron = "*/30 * * * * *")
    public void updateShow() throws IOException {

        URL mirrorUrl = new URL(GET_MIRROR_URL);
        URLConnection mirrorConnection = mirrorUrl.openConnection();
        Document mirror = getTVShowDataJob.parseXML(mirrorConnection.getInputStream());
        if (mirror == null) {
            return;
        }

        NodeList mirrorNodeList = mirror.getElementsByTagName("Mirror");

        // Mascaras definidas pela API. 1 é mirror com XML, 2 é mirror com banner e 4 é mirror com zip
        for (int i = 0; i < mirrorNodeList.getLength(); i++) {
            Node mirrorNode = mirrorNodeList.item(i);
            NodeList mirrorChild = mirrorNode.getChildNodes();
            String url = null;
            for (int j = 0; j < mirrorChild.getLength(); j++) {

                if (mirrorChild.item(j).getNodeName().equals("mirrorpath")) {
                    url = mirrorChild.item(j).getTextContent();
                }
                if (mirrorChild.item(j).getNodeName().equals("typemask")) {
                    int maskValue = Integer.valueOf(mirrorChild.item(j).getTextContent());
                    if ((maskValue & XML_FILE) == XML_FILE) {
                        xmlMirror = url;
                        getTVShowDataJob.setXmlMirror(xmlMirror);
                    }

                    if ((maskValue & BANNER_FILE) == BANNER_FILE) {
                        bannerMirror = url;
                        getTVShowDataJob.setXmlMirror(bannerMirror);
                    }
                }
            }

        }

        TVDBData tvdbData = tvDBRepository.findById();

        if (tvdbData == null) {
            timeUpdate = "1";
        } else {
            timeUpdate = tvdbData.getLastUpdateTime();
        }

        URL updateUrl = new URL(GET_UPDATE_URL + timeUpdate);
        URLConnection updateConnection = updateUrl.openConnection();

        Document update = getTVShowDataJob.parseXML(updateConnection.getInputStream());
        if (update == null) {
            return;
        }

        NodeList descNodes = update.getElementsByTagName("Time");

        for (int i = 0; i < descNodes.getLength(); i++) {
            timeUpdate = descNodes.item(i).getTextContent();
        }

        NodeList seriesNode = update.getElementsByTagName("Series");
        System.out.println("Updated TVShows = " + seriesNode.getLength());
        for (int i = 0; i < seriesNode.getLength(); i++) {
            String baseUrl = GET_SERIES_UPDATE.replace("%xmlmirror%", xmlMirror). //
                    replace("%seriesid%", seriesNode.item(i).getTextContent());
            String englishUrl = baseUrl.replace("%language%", "en");
            String portugueseUrl = baseUrl.replace("%language%", "pt");
            TvShow tvShow = getTvShow(seriesNode.item(i).getTextContent(), englishUrl);
            if (tvShow != null) {
                processSeries(seriesNode.item(i).getTextContent(), englishUrl, "en", tvShow);
                processSeries(seriesNode.item(i).getTextContent(), portugueseUrl, "pt", tvShow);
                baseUrl = GET_BANNER_UPDATE.replace("%xmlmirror%", xmlMirror). //
                        replace("%seriesid%", seriesNode.item(i).getTextContent());
                processBanners(seriesNode.item(i).getTextContent(), baseUrl);
                baseUrl = GET_ACTORS_UPDATE.replace("%xmlmirror%", xmlMirror). //
                        replace("%seriesid%", seriesNode.item(i).getTextContent());
                processActors(seriesNode.item(i).getTextContent(), baseUrl);
                addSerie(seriesNode.item(i).getTextContent(), englishUrl);
            } else {
                addSerie(seriesNode.item(i).getTextContent(), englishUrl);
            }
        }

        NodeList episodeNode = update.getElementsByTagName("Episode");
        System.out.println("Updated Episodes = " + episodeNode.getLength());
        for (int i = 0; i < episodeNode.getLength(); i++) {
            String url = GET_EPISODES_UPDATE.replace("%xmlmirror%", xmlMirror). //
                    replace("%episodesid%", episodeNode.item(i).getTextContent());
            url = url.replace("%language%", "pt");
            addEpisode(episodeNode.item(i).getTextContent(), url, "pt");
        }

        if (tvdbData == null) {
            tvdbData = new TVDBData();
        }
        tvdbData.setLastUpdateTime(timeUpdate);
        tvdbData.setMirror(xmlMirror);
        tvdbData.setLastUpdate(new Date());
        tvDBRepository.addTVDBData(tvdbData);
        sendEmail();
    }

    private void processBanners(String id, String url) throws IOException {
        URL updateUrl = new URL(url);
        URLConnection updateConnection = updateUrl.openConnection();
        Document banner = getTVShowDataJob.parseXML(updateConnection.getInputStream());
        if (banner != null) {
            getTVShowDataJob.saveBannersXML(banner, id, null, null, null, bannerMirror);
        }

    }

    private void processActors(String id, String url) throws IOException {
        URL updateUrl = new URL(url);
        URLConnection updateConnection = updateUrl.openConnection();
        if (getTVShowDataJob.getBannerMirror() == null || getTVShowDataJob.getBannerMirror().isEmpty()) {
            getTVShowDataJob.setBannerMirror(xmlMirror);
        }
        Document actors = getTVShowDataJob.parseXML(updateConnection.getInputStream());
        if (actors != null) {
            getTVShowDataJob.saveActorsXML(actors, id, bannerMirror);
        }

    }

    private void addEpisode(String id, String url, String language) throws IOException {
        URL updateUrl = new URL(url);
        URLConnection updateConnection = updateUrl.openConnection();
        Document episodeDocument = getTVShowDataJob.parseXML(updateConnection.getInputStream());
        NodeList episodeData = episodeDocument.getElementsByTagName("Episode");
        Node showData = episodeData.item(0);
        NodeList attributes = showData.getChildNodes();

        String seasonId = null;
        String seriesId = null;
        String name = null;
        Integer episodeNumber = null;
        Integer seasonNumber = null;
        String overview = null;
        String[] guestStars = new String[0];
        String[] directors = new String[0];
        String[] writers = new String[0];
        Date firstAired = null;
        TvShow tvShow = null;
        Season season = null;
        Episode episodeEntity = null;
        String filename = null;

        episodeEntity = getEpisode(id);

        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            if (item.getNodeName().equals("seasonid")) {
                seasonId = item.getTextContent();
                season = getSeason(seasonId);
            } else if (item.getNodeName().equals("EpisodeNumber")) {
                if (!item.getTextContent().isEmpty()) {
                    episodeNumber = Integer.valueOf(item.getTextContent());
                }
            } else if (item.getNodeName().equals("EpisodeName")) {
                name = item.getTextContent();
            } else if (item.getNodeName().equals("FirstAired")) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (!item.getTextContent().isEmpty()) {
                        firstAired = formatter.parse(item.getTextContent());
                    }
                } catch (ParseException e) {
                    System.err.println("Nao foi possivel fazer o parse da data.");
                }
            } else if (item.getNodeName().equals("GuestStars")) {
                guestStars = item.getTextContent().split("\\|");
            } else if (item.getNodeName().equals("Director")) {
                directors = item.getTextContent().split("\\|");
            } else if (item.getNodeName().equals("Writer")) {
                writers = item.getTextContent().split("\\|");
            } else if (item.getNodeName().equals("Overview")) {
                overview = item.getTextContent();
            } else if (item.getNodeName().equals("seriesid")) {
                seriesId = item.getTextContent();
                String tvShowURL = GET_SERIES_UPDATE.replace("%xmlmirror%", xmlMirror). //
                        replace("%seriesid%", seriesId);
                tvShowURL = tvShowURL.replace("%language%", "en");
                tvShow = getTvShow(seriesId, tvShowURL);
                if (tvShow == null) {
                    addSerie(seriesId, tvShowURL);
                    return;
                }
            } else if (item.getNodeName().equals("SeasonNumber")) {
                if (!item.getTextContent().isEmpty()) {
                    seasonNumber = Integer.valueOf(item.getTextContent());
                }
            } else if (item.getNodeName().equals("filename")) {
                filename = item.getTextContent();
            }
        }

        String episodeUrl = null;
        if (filename != null && !filename.isEmpty()) {
            episodeUrl = getTVShowDataJob.saveBannersXML(null, seriesId, id, String.valueOf(seasonNumber), filename, bannerMirror);
        }

        if (season == null) {
            season = new Season();
            season.setId(seasonId);
            season.setDateFirst(firstAired);
            season.setDateLast(firstAired);
            season.setSeasonNumber(seasonNumber);
            season.setSeriesID(seriesId);
            seasonRepository.addSeasonData(season);
            Set<String> seasons = tvShow.getSeasons();
            if (seasons != null) {
                seasons.add(season.getId());
            } else {
                seasons = new HashSet<String>();
                seasons.add(season.getId());
            }
            tvShow.setSeasons(seasons);
            tvShowRepository.addTVShowData(tvShow);
        }

        if (episodeEntity == null) {
            episodeEntity = new Episode();
            episodeEntity.setId(id);
            episodeEntity.setEpisodeNumber(episodeNumber);
            episodeEntity.setSeriesID(seriesId);
            episodeEntity.setSeasonID(seasonId);
            episodeEntity.setSeasonNumber(seasonNumber);
            episodeEntity.setFirstAired(firstAired);
            episodeEntity.setDirectors(getTVShowDataJob.findCast(directors, "Director"));
            episodeEntity.setGuestStars(getTVShowDataJob.findCast(guestStars, "Actor"));
            episodeEntity.setWriters(getTVShowDataJob.findCast(writers, "Writer"));
            if (language.equals("pt")) {
                episodeEntity.setName_pt(name);
                episodeEntity.setOverview_pt(overview);
            } else {
                episodeEntity.setName_en(name);
                episodeEntity.setOverview_en(overview);
            }
            if (episodeUrl != null) {
                episodeEntity.setUrl(episodeUrl);
            }
            episodeRepository.addEpisodeData(episodeEntity);
            Set<String> episodeList = season.getEpisodes();
            if (episodeList != null) {
                episodeList.add(episodeEntity.getId());
                season.setEpisodes(episodeList);
                if (firstAired != null) {
                    if (season.getDateFirst() == null || !season.getDateFirst().before(firstAired)) {
                        season.setDateFirst(firstAired);
                    } else if (season.getDateLast() == null || !season.getDateLast().after(firstAired)) {
                        season.setDateLast(firstAired);
                    }
                }
            } else {
                episodeList = new HashSet<String>();
                episodeList.add(episodeEntity.getId());
                season.setEpisodes(episodeList);
            }
            seasonRepository.addSeasonData(season);
        } else {

            {

                if (episodeEntity.getEpisodeNumber() == null || !episodeEntity.getEpisodeNumber().equals(episodeNumber)) {
                    episodeEntity.setEpisodeNumber(episodeNumber);
                }

                if (episodeEntity.getSeasonNumber() == null || !episodeEntity.getSeasonNumber().equals(seasonNumber)) {
                    episodeEntity.setSeasonNumber(seasonNumber);
                    episodeEntity.setSeasonID(seasonId);
                }

                if (episodeEntity.getFirstAired() == null || !episodeEntity.getFirstAired().equals(firstAired)) {
                    episodeEntity.setFirstAired(firstAired);
                }

                if (!getTVShowDataJob.checkList(episodeEntity.getDirectors(), getTVShowDataJob.findCast(directors, "Director"))) {
                    episodeEntity.setDirectors(getTVShowDataJob.findCast(directors, "Director"));
                }

                if (!getTVShowDataJob.checkList(episodeEntity.getGuestStars(), getTVShowDataJob.findCast(guestStars, "Actor"))) {
                    episodeEntity.setGuestStars(getTVShowDataJob.findCast(guestStars, "Actor"));
                }

                if (!getTVShowDataJob.checkList(episodeEntity.getWriters(), getTVShowDataJob.findCast(writers, "Writer"))) {
                    episodeEntity.setWriters(getTVShowDataJob.findCast(writers, "Writer"));
                }

                if (episodeEntity.getUrl() == null && episodeUrl != null) {
                    episodeEntity.setUrl(episodeUrl);
                }

                if (language.equals("pt")) {
                    if (episodeEntity.getOverview_pt() == null || !episodeEntity.getOverview_pt().equals(overview)) {
                        episodeEntity.setOverview_pt(overview);
                    }
                    if (episodeEntity.getName_pt() == null || !episodeEntity.getName_pt().equals(name)) {
                        episodeEntity.setName_pt(name);
                    }
                } else {
                    if (episodeEntity.getOverview_en() == null || !episodeEntity.getOverview_en().equals(overview)) {
                        episodeEntity.setOverview_en(overview);
                    }
                    if (episodeEntity.getName_en() == null || !episodeEntity.getName_en().equals(name)) {
                        episodeEntity.setName_en(name);
                    }
                }
                episodeRepository.addEpisodeData(episodeEntity);
            }
        }

    }

    private Episode getEpisode(String id) {
        System.out.println("Searching Episode id=" + id);
        Episode episode = episodeRepository.findById(id);
        if (episode != null) {
            System.out.println("Found Episode name=" + episode.getName_en() + "|" + episode.getName_pt());
        } else {
            System.out.println("Episode not found");
        }
        return episode;
    }

    private Season getSeason(String id) {
        System.out.println("Searching Season id=" + id);
        Season season = seasonRepository.findById(id);
        if (season != null) {
            System.out.println("Found Season seasonNumber=" + season.getSeasonNumber());
        } else {
            System.out.println("Season not found");
        }
        return season;
    }

    private void processSeries(String id, String url, String language, TvShow tvShow) throws IOException {
        URL updateUrl = new URL(url);
        URLConnection updateConnection = updateUrl.openConnection();
        Document tvShowDocument = getTVShowDataJob.parseXML(updateConnection.getInputStream());
        if (tvShowDocument == null) {
            return;
        }
        NodeList tvShowData = tvShowDocument.getElementsByTagName("Series");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String network = null;
        Date firstAired = null;
        String overview = null;
        Boolean isEnded = null;
        String[] actors = null;
        String airsTime = null;
        String[] genre = null;
        Integer runTime = null;
        Node showData = tvShowData.item(0);
        NodeList attributes = showData.getChildNodes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            if (item.getNodeName().equals("Network")) {
                network = item.getTextContent();
            } else if (item.getNodeName().equals("FirstAired")) {
                if (item.getTextContent() != null && !item.getTextContent().isEmpty()) {
                    try {
                        if (!item.getTextContent().isEmpty()) {
                            firstAired = formatter.parse(item.getTextContent());
                        }
                    } catch (ParseException e) {
                        System.err.println("Nao foi possivel fazer o parse da data.");
                    }
                }
            } else if (item.getNodeName().equals("Overview")) {
                overview = item.getTextContent();
            } else if (item.getNodeName().equals("Status")) {
                isEnded = item.getTextContent().equals("Ended");
            } else if (item.getNodeName().equals("Actors")) {
                actors = item.getTextContent().split("\\|");
            } else if (item.getNodeName().equals("Airs_Time")) {
                airsTime = item.getTextContent();
            } else if (item.getNodeName().equals("Genre")) {
                genre = item.getTextContent().split("\\|");
            } else if (item.getNodeName().equals("Runtime")) {
                if (item.getTextContent() != null && !item.getTextContent().isEmpty()) {
                    runTime = Integer.valueOf(item.getTextContent());
                }
            }
        }

        if (tvShow.getNetwork() == null || !tvShow.getNetwork().equals(network)) {
            tvShow.setNetwork(network);
        }

        if (tvShow.getFirstAired() == null || !tvShow.getFirstAired().equals(firstAired)) {
            tvShow.setFirstAired(firstAired);
        }

        if (language.equals("pt")) {
            if (tvShow.getOverview_pt() == null || !tvShow.getOverview_pt().equals(overview)) {
                tvShow.setOverview_pt(overview);
            }
        } else {
            if (tvShow.getOverview_en() == null || !tvShow.getOverview_en().equals(overview)) {
                tvShow.setOverview_en(overview);
            }
        }

        if (tvShow.getCast() == null || !getTVShowDataJob.checkList(tvShow.getCast(), getTVShowDataJob.findCast(actors, "Actor"))) {
            tvShow.setCast(getTVShowDataJob.findCast(actors, "Actor"));
        }

        if (tvShow.getEnded() == null || !tvShow.getEnded() != isEnded) {
            tvShow.setEnded(isEnded);
        }

        if (tvShow.getAirsTime() == null || !tvShow.getAirsTime().equals(airsTime)) {
            tvShow.setAirsTime(airsTime);
        }

        if (tvShow.getGenre() == null || !getTVShowDataJob.checkList(tvShow.getGenre(), getTVShowDataJob.findGenre(genre))) {
            tvShow.setGenre(getTVShowDataJob.findGenre(genre));
        }

        if (tvShow.getRunTime() == null || !tvShow.getRunTime().equals(runTime)) {
            tvShow.setRunTime(runTime);
        }

        tvShowRepository.addTVShowData(tvShow);
    }

    private TvShow getTvShow(String id, String url) {
        URL updateUrl = null;
        try {
            updateUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        URLConnection updateConnection = null;
        try {
            updateConnection = updateUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Document update;

        try {
            update = getTVShowDataJob.parseXML(updateConnection.getInputStream());
        } catch (FileNotFoundException e) {
            System.err.println("Not possible to get file for tvshow id=" + id);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (update == null) {
            return null;
        }

        NodeList tvShowData = update.getElementsByTagName("Series");

        Node showData = tvShowData.item(0);
        NodeList attributes = showData.getChildNodes();
        NewTVShow newTVShow = new NewTVShow();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            if (item.getNodeName().equals("SeriesName")) {
                if (!item.getTextContent().isEmpty()) {
                    System.out.println("Searching TVShow name=" + item.getTextContent());
                    return tvShowRepository.findByTVShowName(item.getTextContent());
                }
            }
        }
        return null;
    }

    private void addSerie(String id, String url) throws IOException {
        URL updateUrl = new URL(url);
        URLConnection updateConnection = updateUrl.openConnection();
        Document update;

        try {
            update = getTVShowDataJob.parseXML(updateConnection.getInputStream());
        } catch (FileNotFoundException e) {
            System.err.println("Not possible to get file for tvshow id=" + id);
            return;
        }

        if (update == null) {
            return;
        }

        NodeList tvShowData = update.getElementsByTagName("Series");

        Node showData = tvShowData.item(0);
        NodeList attributes = showData.getChildNodes();
        NewTVShow newTVShow = new NewTVShow();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            if (item.getNodeName().equals("SeriesName")) {
                if (!item.getTextContent().isEmpty()) {

                    System.out.println("Adding " + item.getTextContent() + " to newTVShow");
                    newTVShow.setName(item.getTextContent());
                    newTVShowRepository.addNewTVShowData(newTVShow);
                    break;
                }
            }
        }

    }

    private void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("gnrabello@uol.com.br");
        message.setTo("grabello@gmail.com");
        message.setSubject("Números");
        int newTvShowCount = newTVShowRepository.countNewTVShow();
        int tvShowCount = tvShowRepository.countTVShow();
        StringBuffer msg = new StringBuffer();
        msg.append("NewTVShows = ").append(newTvShowCount).append("\n");
        msg.append("TVShows = ").append(tvShowCount).append("\n");
        message.setText(msg.toString());
        mailSender.send(message);

    }
}
