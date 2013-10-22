package com.wikishow.service;

import com.wikishow.entity.*;
import com.wikishow.repository.*;
import com.wikishow.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TVShowInfoService {

    public static final String MAIN_CAST_PT = "Cast principal";
    public static final String MAIN_CAST_EN = "Main cast";
    public static final String GUEST_STAR_EN = "Guest Star";
    public static final String GUEST_STAR_PT = "Ator convidado";
    public static final String DIRECTOR_EN = "Director";
    public static final String DIRECTOR_PT = "Diretor";
    public static final String WRITER_EN = "Writer";
    public static final String WRITER_PT = "Roteirista";
    private static final String ACCESS_KEY = "AKIAJTBPDH4NJDBK7YVQ";
    private static final String SECRET_KEY = "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";
    @Autowired
    TVShowRepository tvShowRepository;
    @Autowired
    EpisodeRepository episodeRepository;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    CastRepository castRepository;
    @Autowired
    RoleRepository roleRepository;

    public TVShowVO getTVShow(String name, String lang) {

        return toTVShowVO(name, lang);
    }

    public TVShowVO getSeason(Integer seasonNumber, TVShowVO tvShowVO, String seasonId, String lang) {
        List<SeasonVO> seasonVOs = tvShowVO.getSeasons();
        if (seasonVOs != null && !seasonVOs.isEmpty()) {
            if (!checkSeason(seasonVOs, seasonNumber)) {
                Season season = seasonRepository.findById(seasonId);
                seasonVOs.add(toSeasonVO(season, lang));
                tvShowVO.setSeasons(seasonVOs);
            }
        } else {
            seasonVOs = new ArrayList<SeasonVO>();
            Season season = seasonRepository.findById(seasonId);
            seasonVOs.add(toSeasonVO(season, lang));
            tvShowVO.setSeasons(seasonVOs);
        }
        return tvShowVO;
    }

//    public SeasonVO getEpisode(Integer episodeNumber, Integer seasonNumber, SeasonVO seasonVO, String episodeId, String lang) {
//        List<EpisodeVO> episodeVOs = seasonVO.getEpisodes();
//        if (episodeVOs != null && !episodeVOs.isEmpty()) {
//            if (checkEpisode(episodeVOs, episodeNumber)) {
//                Episode episode = episodeRepository.findById(episodeId);
//                episodeVOs.add(toEpisodeVO(episode, lang));
//                seasonVO.setEpisodes(episodeVOs);
//            }
//        } else {
//            episodeVOs = new ArrayList<EpisodeVO>();
//            Episode episode = episodeRepository.findById(episodeId);
//            episodeVOs.add(toEpisodeVO(episode, lang));
//            seasonVO.setEpisodes(episodeVOs);
//        }
//        return seasonVO;
//    }

    private boolean checkEpisode(List<EpisodeVO> episodeVOs, Integer episodeNumber) {
        for (EpisodeVO episodeVO : episodeVOs) {
            if (episodeVO.getEpisodeNumber().equals(episodeNumber)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSeason(List<SeasonVO> seasonVOs, Integer seasonNumber) {
        for (SeasonVO seasonVO : seasonVOs) {
            if (seasonVO.getSeasonNumber().equals(seasonNumber)) {
                return true;
            }
        }
        return false;
    }

    public TVShowVO toTVShowVO(String name, String lang) {
        TvShow tvShow = tvShowRepository.findByTVShowName(name);
        String overview;
        Set<String> seasons = tvShow.getSeasons();
        List<Season> seasonList = seasonRepository.findByIds(seasons);
        SortedMap<Integer, String> seasonMap = new TreeMap<Integer, String>();
        for (Season season : seasonList) {
            seasonMap.put(season.getSeasonNumber(), season.getId());
        }
        if (lang.equals("pt")) {
            overview = tvShow.getOverview_pt();
        } else {
            overview = tvShow.getOverview_en();
        }
        Set<String> actors = tvShow.getCast();
        List<RoleVO> roleVOs = new ArrayList<RoleVO>();
        List<Role> roleList = roleRepository.listRoleBySeriesId(tvShow.getId());
        for (Role role : roleList) {
            //Role role = roleRepository.findByActorAndSeriesId(actor, tvShow.getId());
            roleVOs.add(toRoleVO(role, lang));
        }
        StringBuffer s3Key = new StringBuffer();
        s3Key.append(tvShow.getId()).append("/");
        s3Key.append("poster").append("/");
        List<String> banners = getImageURL(s3Key.toString());
        s3Key = new StringBuffer();
        s3Key.append(tvShow.getId()).append("/");
        s3Key.append("series/");
        List<String> series = getImageURL(s3Key.toString());
        System.out.println("m=getTVShow,tvShowName=" + tvShow.getTvShowName());
        //tvShowName, network, firstAired, overview, ended, List<SeasonVO> seasons, List<RoleVO> cast, airsTime, Set<String> genre, runTime, banners, series
        TVShowVO tvShowVO = new TVShowVO(tvShow.getTvShowName(), tvShow.getNetwork(), tvShow.getFirstAired(), overview, tvShow.getEnded(), roleVOs, tvShow.getAirsTime(), tvShow.getGenre(), tvShow.getRunTime(), banners, series, seasonMap);
        return tvShowVO;
    }

    public List<CastAndCrewVO> toCastAndCrewVO(List<CastAndCrew> castAndCrews, String type) {
        System.out.println();
        List<CastAndCrewVO> castAndCrewVOList = new ArrayList<CastAndCrewVO>();
        for (CastAndCrew castAndCrew : castAndCrews) {
            CastAndCrewVO castAndCrewVO = new CastAndCrewVO(castAndCrew.getName(), type);
            System.out.println(castAndCrewVO.toString());
            castAndCrewVOList.add(castAndCrewVO);
        }
        System.out.println("m=toCastAndCrewVO,castAndCrew=" + castAndCrewVOList);
        return castAndCrewVOList;
    }

    public RoleVO toRoleVO(Role role, String lang) {
        CastAndCrewVO castName;
        if (lang.equals("pt")) {
            castName = new CastAndCrewVO(role.getCastName(), MAIN_CAST_PT);
        } else {
            castName = new CastAndCrewVO(role.getCastName(), MAIN_CAST_EN);
        }
        StringBuffer s3Key = new StringBuffer();
        s3Key.append(role.getTvShowId()).append("/cast/");
        s3Key.append(role.getId());
        List<String> imageURL = getImageURL(s3Key.toString());
        String image = null;
        if (imageURL != null && !imageURL.isEmpty()) {
            image = imageURL.get(0);
        }
        System.out.println("m=toRoleVO,role=" + role.getRole());
        return new RoleVO(role.getRole(), castName, image);
    }

    public SeasonVO toSeasonVO(Season season, String lang) {
        Set<String> episodeIdList = season.getEpisodes();
        SortedMap<Integer, EpisodeVO> episodeSortMap = new TreeMap<Integer, EpisodeVO>();

        for (String episodeId : episodeIdList) {
            Episode episode = episodeRepository.findById(episodeId);
            EpisodeVO episodeVO = toBasicEpisodeVO(episode, lang);
            episodeSortMap.put(episode.getEpisodeNumber(), episodeVO);
        }
        StringBuffer s3Key = new StringBuffer();
        s3Key.append(season.getSeriesID()).append("/");
        s3Key.append("seasons/");
        s3Key.append(season.getSeasonNumber() + "/");
        //Integer seasonNumber, Date dateFirst, Date dateLast, List<EpisodeVO> episodes, List<String> seasonURL
        System.out.println("" +
                "" +
                "" +
                "=toSeasonVO,sesonNumber=" + season.getSeasonNumber());
        return new SeasonVO(season.getSeasonNumber(), season.getDateFirst(), season.getDateLast(), getImageURL(s3Key.toString()), episodeSortMap);
    }

    public EpisodeVO toEpisodeVO(Episode episode, String lang) {
        List<CastAndCrewVO> guestStars = null;//;
        List<CastAndCrewVO> directors = null;
        List<CastAndCrewVO> writers = null;
        String episodeName;
        String overview;
        String episodeURL = null;

        if (lang.equals("pt")) {
            episodeName = episode.getName_pt();
            overview = episode.getOverview_pt();
            guestStars = toCastAndCrewVO(castRepository.findByIds(episode.getGuestStars()), GUEST_STAR_PT);
            directors = toCastAndCrewVO(castRepository.findByIds(episode.getDirectors()), DIRECTOR_PT);
            writers = toCastAndCrewVO(castRepository.findByIds(episode.getWriters()), WRITER_PT);
        } else {
            episodeName = episode.getName_en();
            overview = episode.getOverview_en();
            guestStars = toCastAndCrewVO(castRepository.findByIds(episode.getGuestStars()), GUEST_STAR_EN);
            directors = toCastAndCrewVO(castRepository.findByIds(episode.getDirectors()), DIRECTOR_EN);
            writers = toCastAndCrewVO(castRepository.findByIds(episode.getWriters()), WRITER_EN);
        }
        StringBuffer s3Key = new StringBuffer();
        s3Key.append(episode.getSeriesID()).append("/");
        s3Key.append("episodes/").append(episode.getSeasonNumber()).append("/");
        s3Key.append(episode.getId() + ".jpg");
        List<String> imageURL = getImageURL(s3Key.toString());
        if (imageURL != null) {
            episodeURL = imageURL.get(0);
        }
        System.out.println("m=toEpisodeVO,episodeName=" + episodeName);
        //name, episodeNumber, overview, List<CastAndCrewVO> guestStars, List<CastAndCrewVO> directors, List<CastAndCrewVO> writers, firstAired, episodeImage
        return new EpisodeVO(episodeName, episode.getEpisodeNumber(), overview, guestStars, directors, writers, episode.getFirstAired(), episodeURL);
    }

    public EpisodeVO completeEpisodeVO(EpisodeVO episodeVO, String lang) {
        List<CastAndCrewVO> guestStars = null;//;
        List<CastAndCrewVO> directors = null;
        List<CastAndCrewVO> writers = null;
        String overview;
        Episode episode = episodeRepository.findById(episodeVO.getId());

        if (lang.equals("pt")) {
            overview = episode.getOverview_pt();
            guestStars = toCastAndCrewVO(castRepository.findByIds(episode.getGuestStars()), GUEST_STAR_PT);
            directors = toCastAndCrewVO(castRepository.findByIds(episode.getDirectors()), DIRECTOR_PT);
            writers = toCastAndCrewVO(castRepository.findByIds(episode.getWriters()), WRITER_PT);
        } else {
            overview = episode.getOverview_en();
            guestStars = toCastAndCrewVO(castRepository.findByIds(episode.getGuestStars()), GUEST_STAR_EN);
            directors = toCastAndCrewVO(castRepository.findByIds(episode.getDirectors()), DIRECTOR_EN);
            writers = toCastAndCrewVO(castRepository.findByIds(episode.getWriters()), WRITER_EN);
        }
        episodeVO.setOverview(overview);
        episodeVO.setGuestStars(guestStars);
        episodeVO.setDirectors(directors);
        episodeVO.setWriters(writers);
        System.out.println("m=completeEpisodeVO,episodeName=" + episodeVO.getName());
        return episodeVO;

    }

    public EpisodeVO toBasicEpisodeVO(Episode episode, String lang) {
        String episodeName;
        String episodeURL = null;

        if (lang.equals("pt")) {
            episodeName = episode.getName_pt();
        } else {
            episodeName = episode.getName_en();
        }
        StringBuffer s3Key = new StringBuffer();
        s3Key.append(episode.getSeriesID()).append("/");
        s3Key.append("episodes/").append(episode.getSeasonNumber()).append("/");
        s3Key.append(episode.getId() + ".jpg");
        List<String> imageURL = getImageURL(s3Key.toString());
        if (imageURL != null) {
            episodeURL = imageURL.get(0);
        }
        System.out.println("m=toEpisodeVO,episodeName=" + episodeName);
        //name, episodeNumber, overview, List<CastAndCrewVO> guestStars, List<CastAndCrewVO> directors, List<CastAndCrewVO> writers, firstAired, episodeImage
        return new EpisodeVO(episode.getId(), episodeName, episode.getEpisodeNumber(), episode.getFirstAired(), episodeURL);
    }


    public List<String> getImageURL(String key) {
        System.out.println("Starting getImageURL key=" + key);
//        AmazonS3Client s3 = new AmazonS3Client(
//                new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
//        if (!s3.doesBucketExist("2ndscreentvshow")) {
//            s3.createBucket("2ndscreentvshow");
//        }
//
//        List<String> imagesURList = new ArrayList<String>();
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
//                .withBucketName("2ndscreentvshow")
//                .withPrefix(key);
//        ObjectListing objectListing;
//        do {
//            objectListing = s3.listObjects(listObjectsRequest);
//            if (objectListing.getObjectSummaries().isEmpty()) {
//                System.err.println("Image not found");
//                return null;
//            }
//            for (S3ObjectSummary objectSummary :
//                    objectListing.getObjectSummaries()) {
//                imagesURList.add(objectSummary.getKey());
//            }
//            listObjectsRequest.setMarker(objectListing.getNextMarker());
//        } while (objectListing.isTruncated());
//        System.out.println("Finishing getImageURL size=" + imagesURList.size());
//        return imagesURList;
        return null;
    }
}
