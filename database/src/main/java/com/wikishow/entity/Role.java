package com.wikishow.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/2/13
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Document
public class Role {

    @Id
    private BigInteger id;
    @Indexed
    private String role;
    private String tvShowId;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(String tvShowId) {
        this.tvShowId = tvShowId;
    }
}
