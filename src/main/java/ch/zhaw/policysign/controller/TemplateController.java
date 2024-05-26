package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.model.Template;
import ch.zhaw.policysign.service.EmailService;
import ch.zhaw.policysign.service.PolicyDocumentService;
import ch.zhaw.policysign.service.RoleService;
import ch.zhaw.policysign.service.S3Service;
import ch.zhaw.policysign.service.TemplateService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TemplateController {
    
    private static final Logger logger = Logger.getLogger(TemplateController.class.getName());

    @Autowired
    private TemplateService templateService;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PolicyDocumentService policyDocumentService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Template> createTemplate(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("xSignature") float xSignature,
            @RequestParam("ySignature") float ySignature,
            @RequestParam("signatureWidth") float signatureWidth,
            @RequestParam("userId") String userId,
            @AuthenticationPrincipal Jwt jwt) throws MessagingException, IOException {


        if (!roleService.hasRole("user", jwt)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        logger.info("Creating template");
        String fileName = UUID.randomUUID().toString() + ".pdf"; // Use UUID for unique file names
        s3Service.uploadFileToS3(file, "templatespolicysign", fileName);

        logger.info("uploaded file to S3");
        Template template = new Template();
        template.setTitle(title);
        template.setDescription(description);
        template.setXSignature(xSignature);
        template.setYSignature(ySignature);
        template.setSignatureWidth(signatureWidth);
        template.setUserId(userId);
        template.setUrl(fileName);
        logger.info("saving template");

        return new ResponseEntity<>(templateService.saveTemplate(template), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Template> getTemplateById(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        if (!roleService.hasRole("user", jwt)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<Template> template = templateService.getTemplateById(id);
        return template.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public List<Template> getTemplatesByUserId(@PathVariable String userId) {
        return templateService.getTemplatesByUserId(userId);
    }


    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable String id) {
        templateService.deleteTemplate(id);
    }

    @PostMapping("/use/{id}")
    public String useTemplate(@PathVariable String id, @RequestParam("link") boolean link, @RequestParam("signedByEmail") String email, @RequestParam("userId") String userId) throws Exception {
        Optional<Template> templateOpt = templateService.getTemplateById(id);
    
        if (templateOpt.isEmpty()) {
            throw new Exception("Template not found");
        }
    
        Template template = templateOpt.get();
    
        // Ensure the userId matches the template's userId
        if (!template.getUserId().equals(userId)) {
            throw new Exception("Unauthorized access");
        }
    
        // Download file from S3 templates-policysign bucket
        byte[] fileBytes = downloadFileFromS3(template.getUrl());
    
        // Upload the downloaded file to S3 policysign bucket
        String newFileName = UUID.randomUUID().toString() + ".pdf";
        s3Service.uploadFileToS3(fileBytes, "policysign", newFileName);
    
        PolicyDocument policyDocument = new PolicyDocument();
        policyDocument.setUrl(newFileName);
        policyDocument.setXSignature(template.getXSignature());
        policyDocument.setYSignature(template.getYSignature());
        policyDocument.setSignatureWidth(template.getSignatureWidth());
        policyDocument.setUserId(template.getUserId());
        policyDocument.setTitle(template.getTitle());
        policyDocument.setDescription(template.getDescription());
        policyDocument.setSignedByEmail(email);
    
        PolicyDocument savedDocument = policyDocumentService.savePolicyDocument(policyDocument);
    
        if (link) {
            return "https://policysign.azurewebsites.net/signature?id=" + savedDocument.getId();
        } else {
            String emailContent = "<html>" +
                    "<body>" +
                    "<h3>Dear User,</h3>" +
                    "<p>A new policy document requires your signature.</p>" +
                    "<p><b>Description:</b> " + savedDocument.getDescription() + "</p>" +
                    "<p>Please review and sign the document at your earliest convenience.</p>" +
                    "<p><a href='https://policysign.azurewebsites.net/signature?id=" + savedDocument.getId() + "'>Click here to sign the document</a></p>" +
                    "<p>Thank you.</p>" +
                    "</body>" +
                    "</html>";
    
            emailService.sendHtmlEmail(savedDocument.getSignedByEmail(), savedDocument.getTitle(), emailContent);
            return "Email sent successfully";
        }
    }
    

    private byte[] downloadFileFromS3(String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("templatespolicysign")
                .key(fileName)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
            return s3Object.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }
}
