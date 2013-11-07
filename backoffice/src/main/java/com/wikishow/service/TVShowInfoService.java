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
    @Autowired
    BannersRepository bannerRepository;

    public TVShowVO getTVShow(String name, String lang) {

        return toTVShowVO(name, lang);
    }

    public TVShowVO getSeason(SeasonVO seasonVO, TVShowVO tvShowVO, String seasonId, String lang) {

        Season season = seasonRepository.findById(seasonId);
        seasonVO = toCompleteSeasonVO(seasonVO, season, lang);
        Map<Integer, SeasonVO> seasonVOMap = tvShowVO.getSeasonMap();
        seasonVOMap.put(seasonVO.getSeasonNumber(), seasonVO);

        return tvShowVO;
    }

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
        if (tvShow == null) {
            return null;
        }
        Set<String> seasons = tvShow.getSeasons();
        List<Season> seasonList = seasonRepository.findByIds(seasons);
        SortedMap<Integer, SeasonVO> seasonMap = new TreeMap<Integer, SeasonVO>();
        if (seasonList != null && !seasonList.isEmpty()) {
            for (Season season : seasonList) {
                seasonMap.put(season.getSeasonNumber(), toBasicSeasonVO(season));
            }
        }
        if (lang.equals("pt")) {
            overview = tvShow.getOverview_pt();
        } else {
            overview = tvShow.getOverview_en();
        }
        Set<String> actors = tvShow.getCast();
        List<RoleVO> roleVOs = new ArrayList<RoleVO>();
        List<Role> roleList = roleRepository.listRoleBySeriesId(tvShow.getId());
        if (roleList != null && !roleList.isEmpty()) {
            for (Role role : roleList) {
                //Role role = roleRepository.findByActorAndSeriesId(actor, tvShow.getId());
                roleVOs.add(toRoleVO(role, lang));
            }
        }
        List<Banners> seriesBanners = bannerRepository.findBySeriesIDAndType(tvShow.getId(), "series");
        List<String> series = null;
        if (seriesBanners != null && !seriesBanners.isEmpty()) {
            series = new ArrayList<String>();
            for (Banners serieBanner : seriesBanners) {
                series.add(serieBanner.getUrl());
            }
        }
//        List<Banners> posterBanners = bannerRepository.findBySeriesIDAndType(tvShow.getId(), "poster");
//        List<String> posters = null;
//        if (posterBanners != null && !posterBanners.isEmpty()) {
//            posters = new ArrayList<String>();
//            for (Banners posterBanner : posterBanners) {
//                posters.add(posterBanner.getUrl());
//            }
//        }
        System.out.println("m=getTVShow,tvShowName=" + tvShow.getTvShowName());
        //tvShowName, network, firstAired, overview, ended, List<SeasonVO> seasons, List<RoleVO> cast, airsTime, Set<String> genre, runTime, banners, series
        TVShowVO tvShowVO = new TVShowVO(tvShow.getTvShowName(), tvShow.getNetwork(), tvShow.getFirstAired(), overview, tvShow.getEnded(), roleVOs, tvShow.getAirsTime(), tvShow.getGenre(), tvShow.getRunTime(), null, series, seasonMap);
        return tvShowVO;
    }

    public List<CastAndCrewVO> toCastAndCrewVO(List<CastAndCrew> castAndCrews, String type) {
        System.out.println();
        List<CastAndCrewVO> castAndCrewVOList = new ArrayList<CastAndCrewVO>();
        if (castAndCrews == null || castAndCrews.isEmpty()) {
            return null;
        }
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
        System.out.println("m=toRoleVO,role=" + role.getRole());
        return new RoleVO(role.getRole(), castName, role.getUrl());
    }

    public SeasonVO toSeasonVO(Season season, String lang) {
        Set<String> episodeIdList = season.getEpisodes();
        SortedMap<Integer, EpisodeVO> episodeSortMap = new TreeMap<Integer, EpisodeVO>();

        for (String episodeId : episodeIdList) {
            Episode episode = episodeRepository.findById(episodeId);
            EpisodeVO episodeVO = toBasicEpisodeVO(episode, lang);
            episodeSortMap.put(episode.getEpisodeNumber(), episodeVO);
        }

        List<Banners> seasons = bannerRepository.findBySeriesIDAndType(season.getSeriesID(), "season");
        List<String> seasonList = null;
        String pattern = season.getSeriesID() + "/seasons/" + season.getSeasonNumber() + "/.*";
        if (seasons != null) {
            seasonList = new ArrayList<String>();
            for (Banners seasonEntity : seasons) {
                if (seasonEntity.getUrl().matches(pattern)) {
                    seasonList.add(seasonEntity.getUrl());
                }
            }
        }
        List<Banners> seasonsWide = bannerRepository.findBySeriesIDAndType(season.getSeriesID(), "seasonwide");
        List<String> seasonWideList = null;
        if (seasonsWide != null) {
            seasonWideList = new ArrayList<String>();
            for (Banners seasonWide : seasonsWide) {
                if (seasonWide.getUrl().matches(pattern)) {
                    seasonWideList.add(seasonWide.getUrl());
                }
            }
        }

        //Integer seasonNumber, Date dateFirst, Date dateLast, List<EpisodeVO> episodes, List<String> seasonURL
        System.out.println("" +
                "" +
                "" +
                "=toSeasonVO,sesonNumber=" + season.getSeasonNumber());
        return new SeasonVO(season.getSeasonNumber(), season.getDateFirst(), season.getDateLast(), seasonList, seasonWideList, episodeSortMap);
    }

    public SeasonVO toBasicSeasonVO(Season season) {

        List<Banners> seasons = bannerRepository.findBySeriesIDAndType(season.getSeriesID(), "season");
        List<String> seasonList = null;
        String pattern = season.getSeriesID() + "/seasons/" + season.getSeasonNumber() + "/.*";
        if (seasons != null && !seasons.isEmpty()) {
            seasonList = new ArrayList<String>();
            for (Banners seasonEntity : seasons) {
                if (seasonEntity.getUrl().matches(pattern)) {
                    seasonList.add(seasonEntity.getUrl());
                }
            }
        }

        SeasonVO seasonVO = new SeasonVO(season.getId(), season.getSeasonNumber(), seasonList, null);

        if (seasonList != null && !seasonList.isEmpty()) {
            int choose = randomNumber(seasonList.size() - 1);
            System.out.println("Choosed=" + choose);
            seasonVO.setChooseSeasonURL(seasonList.get(choose));
        }

        //Integer seasonNumber, Date dateFirst, Date dateLast, List<EpisodeVO> episodes, List<String> seasonURL
        System.out.println("" +
                "" +
                "" +
                "=toSeasonVO,sesonNumber=" + season.getSeasonNumber());
        return seasonVO;
    }

    private Integer randomNumber(int max) {
        return 0 + (int) (Math.random() * ((max - 0) + 1));
    }

    public SeasonVO toCompleteSeasonVO(SeasonVO seasonVO, Season season, String lang) {
        Set<String> episodeIdList = season.getEpisodes();
        SortedMap<Integer, EpisodeVO> episodeSortMap = new TreeMap<Integer, EpisodeVO>();
        String pattern = season.getSeriesID() + "/seasons/" + season.getSeasonNumber() + "/.*";
        if (episodeIdList != null && !episodeIdList.isEmpty()) {
            for (String episodeId : episodeIdList) {
                Episode episode = episodeRepository.findById(episodeId);
                EpisodeVO episodeVO = toBasicEpisodeVO(episode, lang);
                episodeSortMap.put(episode.getEpisodeNumber(), episodeVO);
            }
        }

        seasonVO.setEpisodeMap(episodeSortMap);

        List<Banners> seasonsWide = bannerRepository.findBySeriesIDAndType(season.getSeriesID(), "seasonwide");
        List<String> seasonWideList = null;
        if (seasonsWide != null && !seasonsWide.isEmpty()) {
            seasonWideList = new ArrayList<String>();
            for (Banners seasonWide : seasonsWide) {
                if (seasonWide.getUrl().matches(pattern)) {
                    seasonWideList.add(seasonWide.getUrl());
                }
            }
        }
        seasonVO.setSeasonWideURL(seasonWideList);

        //Integer seasonNumber, Date dateFirst, Date dateLast, List<EpisodeVO> episodes, List<String> seasonURL
        System.out.println("" +
                "" +
                "" +
                "=toCompleteSeasonVO,sesonNumber=" + season.getSeasonNumber());
        return seasonVO;
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

        if (lang.equals("pt")) {
            episodeName = episode.getName_pt();
        } else {
            episodeName = episode.getName_en();
        }
        System.out.println("m=toEpisodeVO,episodeName=" + episodeName);
        //name, episodeNumber, overview, List<CastAndCrewVO> guestStars, List<CastAndCrewVO> directors, List<CastAndCrewVO> writers, firstAired, episodeImage
        return new EpisodeVO(episode.getId(), episodeName, episode.getEpisodeNumber(), episode.getFirstAired(), episode.getUrl());
    }

}
