package com.wikishow.repository;

import com.wikishow.entity.TVDBData;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/18/13
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class TVDBRepository extends DefaultRepository {

    public void addTVDBData(TVDBData tvdbData) {
        tvdbData.setId("1");
        getMapper();
        mapper.save(tvdbData);
    }

    public TVDBData findById() {
        getMapper();
        return mapper.load(TVDBData.class, "1");
    }

    public void deleteTVDBData(TVDBData tvdbData) {
        getMapper();
        mapper.delete(TVDBData.class);
    }

}
