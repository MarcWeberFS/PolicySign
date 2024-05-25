package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.DocumentStatus;

import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.service.EmailService;
import ch.zhaw.policysign.service.PolicyDocumentService;
import ch.zhaw.policysign.service.S3Service;
import jakarta.mail.MessagingException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public List<PolicyDocument> getAllDocuments() {
        return policyDocumentService.getAllPolicyDocuments();
    }

    @GetMapping("/count")
    public long getDocumentCount() {
        return policyDocumentService.getPolicyDocumentCount();
    }

    @PostMapping("/delete/{id}")
    public void deleteDocument(@PathVariable String id) {
        policyDocumentService.deletePolicyDocument(id);
    }

}
