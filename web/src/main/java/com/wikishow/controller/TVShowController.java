package com.wikishow.controller;

import com.wikishow.service.TVShowInfoService;
import com.wikishow.vo.EpisodeVO;
import com.wikishow.vo.SeasonVO;
import com.wikishow.vo.TVShowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.SortedMap;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TVShowController {

    @Autowired
    TVShowInfoService tvShowInfoService;

    @RequestMapping(value = {"/tvshow/{name}", //
            "/tvshow/en/{name}"}, method = RequestMethod.GET)
    public String getTVShow(@PathVariable(value = "name") String name, //
                            HttpServletRequest req, HttpServletResponse resp) {
        name = name.replaceAll("_", " ");
        HttpSession session = req.getSession();
        TVShowVO tvShowVO = (TVShowVO) session.getAttribute(name);
        if (tvShowVO == null) {
            tvShowVO = tvShowInfoService.getTVShow(name, "en");
        }
        session.setAttribute(name, tvShowVO);
        req.setAttribute("json", tvShowVO.toString());
        return "loggedin";
    }

    @RequestMapping(value = "/tvshow/pt/{name}", method = RequestMethod.GET)
    public String getPTTVShow(@PathVariable(value = "name") String name,
                              HttpServletRequest req, HttpServletResponse resp) {
        name = name.replaceAll("_", " ");
        HttpSession session = req.getSession();
        TVShowVO tvShowVO = (TVShowVO) session.getAttribute(name);
        if (tvShowVO == null) {
            tvShowVO = tvShowInfoService.getTVShow(name, "pt");
        }
        session.setAttribute(name, tvShowVO);
        req.setAttribute("json", tvShowVO.toString());
        return "loggedin";
    }

    @RequestMapping(value = {"/tvshow/{name}/{season}", //
            "/tvshow/en/{name}/{season}"}, method = RequestMethod.GET)
    public String getSeason(@PathVariable(value = "name") String name, //
                            @PathVariable(value = "season") Integer season, //
                            HttpServletRequest req, HttpServletResponse resp) {
        name = name.replaceAll("_", " ");
        HttpSession session = req.getSession();
        TVShowVO tvShowVO = (TVShowVO) session.getAttribute(name);
        if (tvShowVO == null) {
            tvShowVO = tvShowInfoService.getTVShow(name, "en");
        }

        SortedMap<Integer, String> seasonMap = tvShowVO.getSeasonMap();
        String seasonId = seasonMap.get(season);
        tvShowVO = tvShowInfoService.getSeason(season, tvShowVO, seasonId, "en");
        session.setAttribute(name, tvShowVO);
        req.setAttribute("json", tvShowVO.toString());
        return "loggedin";
    }

    @RequestMapping(value = "/tvshow/pt/{name}/{season}", method = RequestMethod.GET)
    public String getPTSeason(@PathVariable(value = "name") String name, //
                              @PathVariable(value = "season") Integer season, //
                              HttpServletRequest req, HttpServletResponse resp) {
        name = name.replaceAll("_", " ");
        HttpSession session = req.getSession();
        TVShowVO tvShowVO = (TVShowVO) session.getAttribute(name);
        if (tvShowVO == null) {
            tvShowVO = tvShowInfoService.getTVShow(name, "pt");
        }

        SortedMap<Integer, String> seasonMap = tvShowVO.getSeasonMap();
        String seasonId = seasonMap.get(season);
        tvShowVO = tvShowInfoService.getSeason(season, tvShowVO, seasonId, "pt");
        session.setAttribute(name, tvShowVO);
        req.setAttribute("json", tvShowVO.toString());
        return "loggedin";
    }

    @RequestMapping(value = {"/tvshow/{name}/{season}/{episode}", //
            "/tvshow/en/{name}/{season}/{episode}"}, method = RequestMethod.GET)
    public String getEpisode(@PathVariable(value = "name") String name, //
                             @PathVariable(value = "season") Integer season, //
                             @PathVariable(value = "episode") Integer episode, //
                             HttpServletRequest req, HttpServletResponse resp) {
        name = name.replaceAll("_", " ");
        HttpSession session = req.getSession();
        TVShowVO tvShowVO = (TVShowVO) session.getAttribute(name);
        if (tvShowVO == null) {
            tvShowVO = tvShowInfoService.getTVShow(name, "en");
        }

        if (season != null) {
            SortedMap<Integer, String> seasonMap = tvShowVO.getSeasonMap();
            String seasonId = seasonMap.get(season);
            tvShowVO = tvShowInfoService.getSeason(season, tvShowVO, seasonId, "en");
        }

        if (episode != null) {
            SeasonVO seasonVO = getSeasonVO(tvShowVO.getSeasons(), season);
            SortedMap<Integer, EpisodeVO> episodeMap = seasonVO.getEpisodeMap();
            episodeMap.put(episode, tvShowInfoService.completeEpisodeVO(episodeMap.get(episode), "en"));
        }
        session.setAttribute(name, tvShowVO);
        req.setAttribute("json", tvShowVO.toString());
        return "loggedin";
    }

    @RequestMapping(value = "/tvshow/pt/{name}/{season}/{episode}", method = RequestMethod.GET)
    public String getPTEpisode(@PathVariable(value = "name") String name, //
                             @PathVariable(value = "season") Integer season, //
                             @PathVariable(value = "episode") Integer episode, //
                             HttpServletRequest req, HttpServletResponse resp) {
        name = name.replaceAll("_", " ");
        HttpSession session = req.getSession();
        TVShowVO tvShowVO = (TVShowVO) session.getAttribute(name);
        if (tvShowVO == null) {
            tvShowVO = tvShowInfoService.getTVShow(name, "pt");
        }

        if (season != null) {
            SortedMap<Integer, String> seasonMap = tvShowVO.getSeasonMap();
            String seasonId = seasonMap.get(season);
            tvShowVO = tvShowInfoService.getSeason(season, tvShowVO, seasonId, "pt");
        }

        if (episode != null) {
            SeasonVO seasonVO = getSeasonVO(tvShowVO.getSeasons(), season);
            SortedMap<Integer, EpisodeVO> episodeMap = seasonVO.getEpisodeMap();
            episodeMap.put(episode, tvShowInfoService.completeEpisodeVO(episodeMap.get(episode), "pt"));
        }
        session.setAttribute(name, tvShowVO);
        req.setAttribute("json", tvShowVO.toString());
        return "loggedin";
    }

    private SeasonVO getSeasonVO(List<SeasonVO> seasonVOs, Integer seasonNumber) {
        for (SeasonVO seasonVO : seasonVOs) {
            if (seasonVO.getSeasonNumber().equals(seasonNumber)) {
                return seasonVO;
            }
        }
        return null;
    }
}
