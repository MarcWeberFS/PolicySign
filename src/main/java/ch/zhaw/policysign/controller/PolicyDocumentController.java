package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.DocumentStatus;
import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.service.PolicyDocumentService;
import ch.zhaw.policysign.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import jakarta.mail.MessagingException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

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
    private S3Client s3Client;

    @PostMapping
    public PolicyDocument submitPolicyDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("signedByEmail") String email,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("xSignature") float xSignature,
            @RequestParam("ySignature") float ySignature,
            @RequestParam("signatureWidth") float signatureWidth,
            @RequestParam("userId") String userId) throws MessagingException, IOException {
        
        String fileName = UUID.randomUUID().toString() + ".pdf"; // Use UUID for unique file names
        String fileUrl = uploadFileToS3(file, fileName);

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
                "<p><a href='http://localhost:8080/signature?id=" + savedDocument.getId() + "'>Click here to sign the document</a></p>" +
                "<p>Thank you.</p>" +
                "</body>" +
                "</html>";

        // Send email notification
        emailService.sendHtmlEmail(savedDocument.getSignedByEmail(), savedDocument.getTitle(), emailContent);

        return savedDocument;
    }

    @GetMapping("/user/{userId}")
    public List<PolicyDocument> getDocumentsByUserId(@PathVariable String userId) {
        System.out.println("Getting documents for user ID: " + userId);
        return policyDocumentService.getDocumentsByUserId(userId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadDocument(@PathVariable String id) {
        PolicyDocument document = policyDocumentService.getPolicyDocumentById(id);

        if (document.getUrl() == null || document.getUrl().isEmpty()) {
            throw new IllegalArgumentException("The document URL is null or empty");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("policysign")
                .key(document.getUrl())
                .build();

        ResponseInputStream<GetObjectResponse> s3Object;
        try {
            s3Object = s3Client.getObject(getObjectRequest);
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("Document not found in S3", e);
        }

        InputStreamResource resource = new InputStreamResource(s3Object);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getTitle() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    private String uploadFileToS3(MultipartFile file, String fileName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("policysign")
                .key(fileName)
                .contentType(file.getContentType())
                .build();
    
        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
    
        if (response.sdkHttpResponse().isSuccessful()) {
            return fileName; // Return just the key
        } else {
            throw new RuntimeException("Failed to upload file to S3");
        }
    }
}
