package com.wikishow.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/4/13
 * Time: 8:14 AM
 * To change this template use File | Settings | File Templates.
 */
@Document
public class NewImage {
    @Id
    private BigInteger id;
    private String path;
    @Indexed(unique = true)
    private String key;
    private String imageId;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
