package com.wikishow.helper;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 9:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuthCookie {
    private Cookie authCookie;
    private Integer personId;
    private long timestamp;
    private int sessionTime;
    private String ip;

    public AuthCookie(Integer personId, String ip, long timestamp, int sessionTime) {
        super();
        this.personId = personId;
        this.timestamp = timestamp;
        this.sessionTime = sessionTime;
        this.ip = ip;
    }

    public AuthCookie(HttpServletRequest request) {

        this.authCookie = getAuthCookie(request);

        if (authCookie == null) {
            System.err.println("Cookie de autenticacao do usuario nao localizado");
            return;
        }

        String encryptedValue = authCookie.getValue();
        String decryptedValue = null;
        try {
            Crypt crypt = Crypt.getInstance();
            decryptedValue = crypt.decrypt(encryptedValue);
        } catch (Exception e) {
            System.err.println("Erro ao descriptar conteudo do cookie " + encryptedValue);
        }

        String[] cookie = StringUtils.split(decryptedValue, "|");
        if (cookie.length != 4) {
            System.err.println("Conteudo do cookie invalido");
        }

        int idx = 0;
        this.personId = Integer.valueOf(cookie[idx++]);
        this.ip = cookie[idx++];
        this.timestamp = Long.parseLong(cookie[idx++]);
        this.sessionTime = Integer.parseInt(cookie[idx++]);
    }

    private Cookie getAuthCookie(HttpServletRequest request) {
        Cookie authCookie = null;
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {

                String cookieName = cookie.getName();

                if (cookieName == null || !StringUtils.equals(cookieName, CookieUtil.AUTHENTICATION_COOKIE_NAME)) {
                    continue;
                }

                authCookie = cookie;
                break;
            }
        }
        return authCookie;
    }

    public String createHttpOnlyCookie(HttpServletResponse response) {

        StringBuilder decryptedValue = new StringBuilder();
        decryptedValue.append(personId);
        decryptedValue.append("|");
        decryptedValue.append(ip);
        decryptedValue.append("|");
        decryptedValue.append(timestamp);
        decryptedValue.append("|");
        decryptedValue.append(sessionTime);

        String ecryptedValue = null;

        Crypt crypt = Crypt.getInstance();
        ecryptedValue = crypt.encrypt(decryptedValue.toString());

        authCookie = new Cookie(CookieUtil.AUTHENTICATION_COOKIE_NAME, ecryptedValue);
//        authCookie.setDomain(CookieUtil.COOKIE_WIKISHOW_DOMAIN);
        authCookie.setMaxAge(sessionTime * 1000);
        authCookie.setPath("/");
        //response.addCookie(authCookie);
        return CookieUtil.createHttpOnlyCookie(response, authCookie);
    }

    public String updateCookie(HttpServletResponse resp) {
        if (authCookie != null) {
            this.timestamp = Calendar.getInstance().getTimeInMillis();
            return createHttpOnlyCookie(resp);
        }
        return null;
    }

    public String removeCookie(HttpServletResponse resp) {
        if (authCookie != null) {
            this.sessionTime = 0;
            return createHttpOnlyCookie(resp);
        }
        return null;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public int getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(int sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
