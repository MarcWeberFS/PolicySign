package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.User;
import ch.zhaw.policysign.service.UserService;
import ch.zhaw.policysign.service.RoleService;
import ch.zhaw.policysign.util.Auth0TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetUserCountWithAdminRole() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|665338a5cd5cb856cece7e07")
                .build();
        
        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        
        when(userService.getUserCount()).thenReturn(10L);
        when(roleService.hasRole("admin", jwt)).thenReturn(true);

        mockMvc.perform(get("/api/users/count")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }


    @Test
    public void testGetUserCountWithUserRole() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|665338a5cd5cb856cece7e07")
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("admin", jwt)).thenReturn(false);

        mockMvc.perform(get("/api/users/count")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }


    @Test
    public void testCreateUser() throws Exception {

        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|665338a5cd5cb856cece7e07")
                .build();
        
        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        
        when(roleService.hasRole("admin", jwt)).thenReturn(true);

        User user = new User();
        user.setId("123");
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setRoles(List.of("user"));

        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("123")))
                .andExpect(jsonPath("$.username", is("testUser")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.roles[0]", is("user")));
    }
}
