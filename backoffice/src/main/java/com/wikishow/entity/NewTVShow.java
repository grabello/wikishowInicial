package com.wikishow.entity;

import com.wikishow.repository.NewTVShowRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/23/13
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */

@Document
public class NewTVShow {
    @Id
    private BigInteger id;
    @Indexed(unique = true)
    private String name;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
