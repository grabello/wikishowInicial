package com.wikishow.repository;

import com.wikishow.entity.Banners;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/1/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BannersRepository extends DefaultRepository {

    public void addBanner(Banners banners) {
        getMapper();
        mapper.save(banners);
    }

    public Banners findById(String id) {
        getMapper();
        return mapper.load(Banners.class, id);
    }

}
