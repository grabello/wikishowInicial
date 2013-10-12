/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wikishow.controller;

import com.wikishow.helper.AuthCookie;
import com.wikishow.helper.LoginHelper;
import com.wikishow.service.LoginService;
import com.wikishow.vo.PersonVO;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DefaultController {

    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/")
    public String home(HttpServletRequest req, HttpServletResponse resp) {

        String ip = LoginHelper.getIp(req);
        AuthCookie authCookie = new AuthCookie(req);

        if (authCookie.getPersonId() == null) {
            return "index";
        }
        Integer id = authCookie.getPersonId();
        PersonVO personVO = loginService.getUser(id);

        if (personVO != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = LoginHelper.readJsonFromUrl("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + personVO.getAccessToken());
                req.setAttribute("json", jsonObject.get("email"));
            } catch (JSONException e) {
                return "index";
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return "loggedin";
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
