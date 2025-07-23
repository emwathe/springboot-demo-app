package com.example.demo.controller;

import com.example.demo.entity.AppUser;
import com.example.demo.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class AppUserWebController {
    @Autowired
    private AppUserService appUserService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", appUserService.getAllUsers());
        return "app_users";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "app_user_form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("user") AppUser user) {
        appUserService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<AppUser> user = appUserService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "app_user_form";
        } else {
            return "redirect:/users";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return "redirect:/users";
    }
}
