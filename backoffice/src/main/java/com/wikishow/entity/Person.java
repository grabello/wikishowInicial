package com.wikishow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
@DynamoDBTable(tableName = "Person")
public class Person {

    private Integer id;
    private String email;
    private String refreshToken;
    private String accessToken;
    private String type;
    private String name;


    @DynamoDBAttribute(attributeName = "Id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @DynamoDBHashKey(attributeName = "Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "RefreshToken")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @DynamoDBAttribute(attributeName = "AccessToken")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @DynamoDBAttribute(attributeName = "Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static enum LoginType {
        GOOGLE("G"),
        FACEBOOK("F");
        private String type;

        LoginType(String type) {
            this.type = type;
        }

        public static LoginType getByType(String type) {
            LoginType[] types = LoginType.values();
            for (LoginType loginType : types) {
                if (loginType.getType().equals(type)) {
                    return loginType;
                }
            }
            return null;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
