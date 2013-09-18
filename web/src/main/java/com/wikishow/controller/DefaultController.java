/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wikishow.controller;

import com.google.api.client.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Enumeration;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DefaultController {

    @RequestMapping(value = "/")
    public String home(HttpServletRequest req) {

        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                System.out.println("DefaultController: Passing through..." + cookie);
                System.out.println(cookie.getName());
                System.out.println(cookie.getValue());
            }
        }


        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam("token") String token, ModelMap map) {
        if (token == null) {
            token = "Sem token";
        }
        System.out.println("LOGANDO com token=" + token);
        map.addAttribute("json", token);
        return "login";
    }

}
