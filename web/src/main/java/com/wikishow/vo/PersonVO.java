package com.wikishow.vo;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonVO {

    private BigInteger id;
    private String email;
    private String refreshToken;
    private String accessToken;
    private char type;

    public PersonVO(BigInteger id, String email, String refreshToken, String accessToken, char type) {
        this.id = id;
        this.email = email;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.type = type;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }
}
