package com.royalblood.happy6house.controller;

import com.royalblood.happy6house.config.auth.LoginUser;
import com.royalblood.happy6house.config.auth.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, @LoginUser SessionUser user) {
        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "home";
    }
}