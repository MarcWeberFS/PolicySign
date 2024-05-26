package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.User;
import ch.zhaw.policysign.service.RoleService;
import ch.zhaw.policysign.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        System.out.println("Creating user: " + user);
        return userService.saveUser(user);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount(@AuthenticationPrincipal Jwt jwt) {
        if (!roleService.hasRole("admin", jwt)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(userService.getUserCount(), HttpStatus.OK);
    }
}
