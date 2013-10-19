package com.wikishow.repository;

import com.wikishow.entity.Episode;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/22/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class EpisodeRepository extends DefaultRepository {

    public void addEpisodeData(Episode episodeEntity) {
        getMapper();
        mapper.save(episodeEntity);
    }

    public Episode findById(String id) {
        getMapper();
        return mapper.load(Episode.class, id);
    }

    public void deleteEpisode(Episode episodeEntity) {
        getMapper();
        mapper.delete(episodeEntity);
    }
}
