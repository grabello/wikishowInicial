package com.wikishow.vo;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CastAndCrewVO {

    private String name;
    private String type;

    public CastAndCrewVO(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CastAndCrewVO{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
