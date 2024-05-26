package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.DocumentStatus;

import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.service.EmailService;
import ch.zhaw.policysign.service.PolicyDocumentService;
import ch.zhaw.policysign.service.RoleService;
import ch.zhaw.policysign.service.S3Service;
import jakarta.mail.MessagingException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class PolicyDocumentController {

    @Autowired
    private PolicyDocumentService policyDocumentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @PostMapping
    public ResponseEntity submitPolicyDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("signedByEmail") String email,
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

        String fileName = UUID.randomUUID().toString() + ".pdf"; // Use UUID for unique file names
        s3Service.uploadFileToS3(file, "policysign", fileName);

        PolicyDocument policyDocument = new PolicyDocument();
        policyDocument.setCreationDate(new Date());
        policyDocument.setUpdateDate(new Date());
        policyDocument.setStatus(DocumentStatus.PENDING);
        policyDocument.setSignedByEmail(email);
        policyDocument.setTitle(title);
        policyDocument.setDescription(description);
        policyDocument.setUserId(userId);
        policyDocument.setUrl(fileName); // Store only the key
        policyDocument.setXSignature(xSignature);
        policyDocument.setYSignature(ySignature);
        policyDocument.setSignatureWidth(signatureWidth);

        PolicyDocument savedDocument = policyDocumentService.savePolicyDocument(policyDocument);

        // Prepare the HTML email content
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

        // Send email notification
        emailService.sendHtmlEmail(savedDocument.getSignedByEmail(), savedDocument.getTitle(), emailContent);

        return new ResponseEntity<>(savedDocument, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PolicyDocument>> getDocumentsByUserId(@PathVariable String userId, @AuthenticationPrincipal Jwt jwt) {

        if (!roleService.hasRole("user", jwt)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        System.out.println("Getting documents for user ID: " + userId);
        return new ResponseEntity<>(policyDocumentService.getDocumentsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadDocument(@PathVariable String id, @RequestParam String token) {
        Jwt jwt = jwtDecoder.decode(token);
        if (!roleService.hasRole("user", jwt)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        PolicyDocument document = policyDocumentService.getPolicyDocumentById(id);

        if (document.getUrl() == null || document.getUrl().isEmpty()) {
            throw new IllegalArgumentException("The document URL is null or empty");
        }

        try {
            byte[] fileBytes = s3Service.downloadFileFromS3(document.getUrl(), "policysign");
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + document.getTitle() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PolicyDocument>> getAllDocuments(@AuthenticationPrincipal Jwt jwt) {
        if (!roleService.hasRole("admin", jwt)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(policyDocumentService.getAllPolicyDocuments(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getDocumentCount(@AuthenticationPrincipal Jwt jwt) {
        if (!roleService.hasRole("admin", jwt)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(policyDocumentService.getPolicyDocumentCount(), HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity deleteDocument(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        if (!roleService.hasRole("admin", jwt)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        policyDocumentService.deletePolicyDocument(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
