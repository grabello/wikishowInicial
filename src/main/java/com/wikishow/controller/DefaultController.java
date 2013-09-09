/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wikishow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DefaultController {

    @RequestMapping(value = "/")
    public String home() {
        System.out.println("DefaultController: Passing through...");
        return "index";
    }

    @RequestMapping(value = "/compare", method = RequestMethod.GET)
    public String compare(@RequestParam("input1") String input1,
                          @RequestParam("input2") String input2, Model model) {

        return "loggedin";
    }


    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirect() {

        return "redirect:login";
    }

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String login(@RequestParam("token") String token, ModelMap map) {
        if (token == null) {
            token = "Sem token";
        }
        map.addAttribute("json", token);
        return "login";
    }

}
