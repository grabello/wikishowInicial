package com.wikishow.repository;

import com.wikishow.entity.Season;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/22/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SeasonRepository extends DefaultRepository {

    public void addSeasonData(Season seasonEntity) {
        getMapper();
        mapper.save(seasonEntity);
    }

    public Season findById(String id) {
        getMapper();
        return mapper.load(Season.class, id);
    }

    public void deleteSeason(Season season) {
        getMapper();
        mapper.delete(season);
    }

}
