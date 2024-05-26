package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.Template;
import ch.zhaw.policysign.service.*;
import ch.zhaw.policysign.util.Auth0TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.S3Client;



import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(TemplateController.class)
public class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemplateService templateService;

    @MockBean
    private S3Client s3Client;

    @MockBean
    private EmailService emailService;

    @MockBean
    private PolicyDocumentService policyDocumentService;

    @MockBean
    private S3Service s3Service;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = Logger.getLogger(TemplateControllerTest.class.getName());

    @Test
    public void testCreateTemplate() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|665338a5cd5cb856cece7e07")
                .build();
        
        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("user", jwt)).thenReturn(true);

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test content".getBytes());

        Template template = new Template();
        template.setId(UUID.randomUUID().toString());
        template.setTitle("Test Template");
        template.setDescription("Test Description");
        template.setXSignature(100);
        template.setYSignature(100);
        template.setSignatureWidth(200);
        template.setUserId("userId");
        template.setUrl("test-url");

        when(templateService.saveTemplate(any(Template.class))).thenReturn(template);

        mockMvc.perform(multipart("/api/templates")
                .file(file)
                .param("title", "Test Template")
                .param("description", "Test Description")
                .param("xSignature", "100")
                .param("ySignature", "100")
                .param("signatureWidth", "200")
                .param("userId", "userId")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test Template")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.userId", is("userId")));
    }

    @Test
    public void testGetTemplateById() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|665338a5cd5cb856cece7e07")
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("user", jwt)).thenReturn(true);

        Template template = new Template();
        template.setId("1");
        template.setTitle("Test Template");
        template.setDescription("Test Description");
        template.setUserId("userId");

        when(templateService.getTemplateById("1")).thenReturn(Optional.of(template));

        mockMvc.perform(get("/api/templates/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Template")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.userId", is("userId")));
    }

    @Test
    public void testGetTemplateByIdNotFound() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|665338a5cd5cb856cece7e07")
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("user", jwt)).thenReturn(true);

        when(templateService.getTemplateById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/templates/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTemplate() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|665338a5cd5cb856cece7e07")
                .build();

                when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
                when(roleService.hasRole("admin", jwt)).thenReturn(true);
        
        doNothing().when(templateService).deleteTemplate("1");

        mockMvc.perform(delete("/api/templates/1").header("Authorization", "Bearer " + token))
                
                .andExpect(status().isOk());
    }

}
