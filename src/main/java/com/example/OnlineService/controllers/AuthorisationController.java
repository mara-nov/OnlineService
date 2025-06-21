package com.example.OnlineService.controllers;

import com.example.OnlineService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authorisation")
@Controller
public class AuthorisationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String authorisationForm() {
        return "signin";
    }

    @PostMapping
    public String authorisationSubmit(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {

        return userService.authenticate(username, password)
                .map(user -> "redirect:/start-page")
                .orElseGet(() -> {
                    model.addAttribute("error", "Неверный логин или пароль.");
                    return "signin";
                });
    }
}

