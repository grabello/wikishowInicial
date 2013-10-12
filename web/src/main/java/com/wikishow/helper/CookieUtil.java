package com.wikishow.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class CookieUtil {
    public static final String AUTHENTICATION_COOKIE_NAME = "wikishow";
    public static final String COOKIE_WIKISHOW_DOMAIN = "localhost:8080/wikishow";
    private static final DateFormat COOKIE_DATE_FORMAT;

    static {
        COOKIE_DATE_FORMAT = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        COOKIE_DATE_FORMAT.setCalendar(cal);
    }

    public static String createHttpOnlyCookie(HttpServletResponse response, Cookie cookie) {
        String httpOnlyCookie = createHttpOnlyCookie(cookie);
        response.addHeader("Set-Cookie", httpOnlyCookie.toString());

        return httpOnlyCookie;
    }

    public static String createHttpOnlyCookie(Cookie cookie) {
        // String com todos os parâmetros do cookie
        StringBuffer header = new StringBuffer();

        if ((cookie.getName() != null) && (!cookie.getName().equals(""))) {
            header.append(cookie.getName());
        }

        if (cookie.getValue() != null) {
            // Valores vazios são permitidos para apagar o cookie
            header.append("=" + cookie.getValue());
        }

        if (cookie.getMaxAge() > -1) {
            Date date = new Date();
            date.setTime(date.getTime() + (1000L * cookie.getMaxAge()));
//            header.append("; Expires=" + COOKIE_DATE_FORMAT.format(date));
        }

//        if (cookie.getDomain() != null) {
//            header.append("; Domain=" + cookie.getDomain());
//        }

//        if (cookie.getPath() != null) {
//            header.append("; Path=" + cookie.getPath());
//        }

        // Todos os cookies devem ser Secure e HttpOnly
        //header.append("; Secure; HttpOnly");

        return header.toString();
    }
}
