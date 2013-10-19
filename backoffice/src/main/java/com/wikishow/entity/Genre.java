package com.wikishow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "Genre")
public class Genre {

    private String genre;

    @DynamoDBHashKey(attributeName = "Genre")
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
