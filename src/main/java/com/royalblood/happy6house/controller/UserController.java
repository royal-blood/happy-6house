package com.royalblood.happy6house.controller;

import com.royalblood.happy6house.controller.dto.UserForm;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/new")
    public String createForm() {
        return "users/createUserForm";
    }

    @PostMapping(value = "/users/new")
    public String create(UserForm form) {
        User user = new User();
        user.setName(form.getName());
        user.setNickname(form.getNickname());

        userService.join(user);

        return "redirect:/";
    }

    @GetMapping(value = "/users")
    public String list(Model model) {
        List<User> users = userService.findUsers();
        model.addAttribute("users", users);
        return "users/userList";
    }
}
