package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.DocumentStatus;
import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.model.Template;
import ch.zhaw.policysign.service.*;
import ch.zhaw.policysign.util.Auth0TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PolicyDocumentController.class)
public class PolicyDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyDocumentService policyDocumentService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private S3Service s3Service;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TemplateService templateService;

    @Test
    public void testSubmitPolicyDocument() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|userId")
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("user", jwt)).thenReturn(true);

        PolicyDocument policyDocument = new PolicyDocument();
        policyDocument.setId(UUID.randomUUID().toString());
        policyDocument.setTitle("Test Document");
        policyDocument.setDescription("Test Description");
        policyDocument.setUrl("test-url");
        policyDocument.setStatus(DocumentStatus.PENDING);
        policyDocument.setCreationDate(new Date());
        policyDocument.setUpdateDate(new Date());
        policyDocument.setUserId("userId");
        policyDocument.setSignedByEmail("signer@example.com");

        when(policyDocumentService.savePolicyDocument(any(PolicyDocument.class))).thenReturn(policyDocument);

        mockMvc.perform(multipart("/api/upload")
                .file("file", "test content".getBytes())
                .param("signedByEmail", "signer@example.com")
                .param("title", "Test Document")
                .param("description", "Test Description")
                .param("xSignature", "10.0")
                .param("ySignature", "20.0")
                .param("signatureWidth", "30.0")
                .param("userId", "userId")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Document")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.signedByEmail", is("signer@example.com")));
    }

    @Test
    public void testGetDocumentsByUserId() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|userId")
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("user", jwt)).thenReturn(true);

        PolicyDocument document = new PolicyDocument();
        document.setId("1");
        document.setTitle("Test Document");
        document.setUserId("userId");

        when(policyDocumentService.getDocumentsByUserId("userId")).thenReturn(List.of(document));

        mockMvc.perform(get("/api/upload/user/userId")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Test Document")));
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|adminId")
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("admin", jwt)).thenReturn(true);

        PolicyDocument document = new PolicyDocument();
        document.setId("1");
        document.setTitle("Test Document");

        when(policyDocumentService.getAllPolicyDocuments()).thenReturn(List.of(document));

        mockMvc.perform(get("/api/upload/all")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Test Document")));
    }

    @Test
    public void testGetDocumentCount() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|adminId")
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("admin", jwt)).thenReturn(true);

        when(policyDocumentService.getPolicyDocumentCount()).thenReturn(5L);

        mockMvc.perform(get("/api/upload/count")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void testDeleteDocument() throws Exception {
        String token = Auth0TokenUtil.getAuth0Token("234795892835625111@example.com", "adminPassword324750872094385711");
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "auth0|adminId")
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
        when(roleService.hasRole("admin", jwt)).thenReturn(true);

        doNothing().when(policyDocumentService).deletePolicyDocument("1");

        mockMvc.perform(post("/api/upload/delete/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
