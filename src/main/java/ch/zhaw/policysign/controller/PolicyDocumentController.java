package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.DocumentStatus;
import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.service.PolicyDocumentService;
import ch.zhaw.policysign.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Date;

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
            @RequestParam("xSignature") int xSignature,
            @RequestParam("ySignature") int ySignature,
            @RequestParam("signatureWidth") int signatureWidth,
            @RequestParam("userId") String userId) throws MessagingException, IOException {
        
        String fileName = file.getOriginalFilename();
        String fileUrl = uploadFileToS3(file, fileName);

        PolicyDocument policyDocument = new PolicyDocument();
        policyDocument.setCreationDate(new Date());
        policyDocument.setUpdateDate(new Date());
        policyDocument.setStatus(DocumentStatus.PENDING);
        policyDocument.setSignedByEmail(email);
        policyDocument.setTitle(title);
        policyDocument.setDescription(description);
        policyDocument.setUserId(userId);
        policyDocument.setUrl(fileUrl);
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
                "<p>Thank you.</p>" +
                "</body>" +
                "</html>";

        // Send email notification
        emailService.sendHtmlEmail(savedDocument.getSignedByEmail(), savedDocument.getTitle(), emailContent);

        return savedDocument;
    }

    private String uploadFileToS3(MultipartFile file, String fileName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("policysign")
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        if (response.sdkHttpResponse().isSuccessful()) {
            return "https://" + "policysign" + ".s3.amazonaws.com/" + fileName + "?timestamp=" + System.currentTimeMillis();
        } else {
            throw new RuntimeException("Failed to upload file to S3");
        }
    }
}
