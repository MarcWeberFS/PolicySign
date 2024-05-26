package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.service.UserService;
import ch.zhaw.policysign.service.RoleService;
import ch.zhaw.policysign.util.Auth0TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @Test
    public void testGetUserCountWithUserRole() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625@example.com", "adminPassword3247508720943857");

        mockMvc.perform(get("/api/users/count")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
