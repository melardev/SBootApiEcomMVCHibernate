package com.melardev.spring.shoppingcartweb.controllers;

import com.melardev.spring.shoppingcartweb.forms.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/users")
public class LoginController {

    @GetMapping("login")
    public String login(Model model) {
        model.addAttribute("form", new LoginForm());
        return "users/login";
    }

}
