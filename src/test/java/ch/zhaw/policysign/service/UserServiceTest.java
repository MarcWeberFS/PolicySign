package ch.zhaw.policysign.service;

import ch.zhaw.policysign.model.User;
import ch.zhaw.policysign.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId("123");
        user.setUsername("testuser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId("123");
        user.setUsername("testuser");

        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById("123");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setId("123");
        user1.setUsername("testuser1");

        User user2 = new User();
        user2.setId("124");
        user2.setUsername("testuser2");

        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("testuser1", users.get(0).getUsername());
        assertEquals("testuser2", users.get(1).getUsername());
    }

    @Test
    public void testGetUserCount() {
        when(userRepository.count()).thenReturn(10L);

        long userCount = userService.getUserCount();
        assertEquals(10L, userCount);
    }
}
