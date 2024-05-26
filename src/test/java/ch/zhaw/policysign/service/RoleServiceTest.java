package ch.zhaw.policysign.service;

import ch.zhaw.policysign.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Mock
    private Jwt jwt;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHasRoleTrue() {
        User user = new User();
        user.setId("123");
        user.setRoles(Arrays.asList("admin", "user"));

        when(jwt.getClaimAsString("sub")).thenReturn("123");
        when(userService.getUserById("123")).thenReturn(Optional.of(user));

        boolean hasRole = roleService.hasRole("admin", jwt);
        assertTrue(hasRole);
    }

    @Test
    public void testHasRoleFalse() {
        User user = new User();
        user.setId("123");
        user.setRoles(Arrays.asList("user"));

        when(jwt.getClaimAsString("sub")).thenReturn("123");
        when(userService.getUserById("123")).thenReturn(Optional.of(user));

        boolean hasRole = roleService.hasRole("admin", jwt);
        assertFalse(hasRole);
    }
}
