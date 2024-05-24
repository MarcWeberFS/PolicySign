package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.User;
import ch.zhaw.policysign.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        System.out.println("Creating user: " + user);
        return userService.saveUser(user);
    }

   @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/count")
    public long getUserCount() {
        return userService.getUserCount();
    }
}
