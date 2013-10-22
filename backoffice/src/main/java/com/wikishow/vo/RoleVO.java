package com.wikishow.vo;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoleVO {

    private String role;
    private CastAndCrewVO castName;
    private String roleImage;

    public RoleVO(String role, CastAndCrewVO castName, String roleImage) {
        this.role = role;
        this.castName = castName;
        this.roleImage = roleImage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public CastAndCrewVO getCastName() {
        return castName;
    }

    public void setCastName(CastAndCrewVO castName) {
        this.castName = castName;
    }

    public String getRoleImage() {
        return roleImage;
    }

    public void setRoleImage(String roleImage) {
        this.roleImage = roleImage;
    }

    @Override
    public String toString() {
        return "RoleVO{" +
                "role='" + role + '\'' +
                ", castName=" + castName +
                ", roleImage='" + roleImage + '\'' +
                '}';
    }
}
