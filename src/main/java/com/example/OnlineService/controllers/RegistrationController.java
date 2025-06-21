package com.example.OnlineService.controllers;

import com.example.OnlineService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegisterForm() {
        return "signup";
    }

    @PostMapping
    public String registerUser(@RequestParam String login,
                               @RequestParam String password,
                               Model model) {
        boolean success = userService.registerUser(login, password);

        if (!success) {
            model.addAttribute("error", "Логин уже используется. Пожалуйста, выберите другой.");
            return "signup";
        }

        userService.autoLogin(login, password);

        return "redirect:/start-page";
    }

}
