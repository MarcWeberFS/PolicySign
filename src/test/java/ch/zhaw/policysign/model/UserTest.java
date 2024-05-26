package ch.zhaw.policysign.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserSettersAndGetters() {
        User user = new User();
        user.setId("123");
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setRoles(List.of("admin", "user"));

        assertEquals("123", user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(List.of("admin", "user"), user.getRoles());
    }

    @Test
    public void testUserToString() {
        User user = new User();
        user.setId("123");
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setRoles(List.of("admin", "user"));

        String expectedString = "User(id=123, username=testUser, email=test@example.com, roles=[admin, user])";
        assertEquals(expectedString, user.toString());
    }
}
