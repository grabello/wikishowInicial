package com.wikishow.entity;

import com.wikishow.repository.TVDBRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/18/13
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Document(collection = TVDBRepository.COLLECTION_NAME)
public class TVDBData {

    @Id
    private String id;
    private String lastUpdateTime;
    private String mirror;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getMirror() {
        return mirror;
    }

    public void setMirror(String mirror) {
        this.mirror = mirror;
    }
}
