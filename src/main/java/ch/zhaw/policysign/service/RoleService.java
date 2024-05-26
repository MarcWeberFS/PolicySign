package ch.zhaw.policysign.service;

import org.springframework.stereotype.Service;
import ch.zhaw.policysign.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;

@Service
public class RoleService {

    @Autowired
    private UserService userService;

    public boolean hasRole(String role, Jwt jwt) {
        String userId = jwt.getClaimAsString("sub");
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            List<String> roles = user.get().getRoles();
            return roles != null && roles.contains(role);
        } else {
            System.out.println("User not found for ID: " + userId);
        }
        return false;
    }
}
