package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.DocumentStatus;
import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.model.Signatory;
import ch.zhaw.policysign.service.PolicyDocumentService;
import ch.zhaw.policysign.service.SignatoryService;
import ch.zhaw.policysign.service.UserService;
import ch.zhaw.policysign.controller.SignatureRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

@RestController
@RequestMapping("/signature")
@CrossOrigin(origins = "*")
public class SignatoryController {

    private static final Logger logger = Logger.getLogger(SignatoryController.class.getName());

    @Autowired
    private SignatoryService signatoryService;

    @Autowired
    private PolicyDocumentService policyDocumentService;

    @Autowired
    private UserService userService;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getDocumentForSignature(@PathVariable String id) {
        logger.info("Fetching policy document for document ID: " + id);

        Optional<PolicyDocument> documentOpt = Optional.of(policyDocumentService.getPolicyDocumentById(id));
        if (documentOpt.isEmpty()) {
            logger.warning("Policy document not found for document ID: " + id);
            return ResponseEntity.notFound().build();
        }

        PolicyDocument document = documentOpt.get();
        logger.info("Policy document found: " + document);
        String fileKey = document.getUrl();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("policysign")
                .key(fileKey)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

        try (InputStream inputStream = s3Object) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            ByteArrayResource resource = new ByteArrayResource(buffer.toByteArray());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getTitle() + "\"")
                    .body(resource);
        } catch (IOException e) {
            logger.severe("Error reading S3 object: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> saveSignature(@PathVariable String id, @RequestBody SignatureRequest signatureRequest) {
        logger.info("Saving signature for document ID: " + id);

        Optional<PolicyDocument> documentOpt = Optional.of(policyDocumentService.getPolicyDocumentById(id));
        if (documentOpt.isEmpty()) {
            logger.warning("Policy document not found for document ID: " + id);
            return ResponseEntity.notFound().build();
        }

        PolicyDocument document = documentOpt.get();
        Signatory signatory = new Signatory();
        signatory.setDocumentId(id);
        signatory.setSigned(true);
        signatory.setSignDate(new java.util.Date());
        signatoryService.saveSignatory(signatory);

        try {
            byte[] signedDocument = signDocument(document, signatureRequest);

            userService.getUserById(document.getUserId()).ifPresent(user -> {
                try {
                    sendEmail(user.getEmail(), signedDocument);
                } catch (MessagingException e) {
                    logger.severe("Error sending email to user: " + e.getMessage());
                }
            });
            // Send email to both parties
            sendEmail(document.getSignedByEmail(), signedDocument);

            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("policysign")
                .key(document.getUrl())
                .contentType("application/pdf")
                .build();

            
            // Because RequestBody annotation is used at the method level used by spring boot, we need to use the following method to send the request body
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(signedDocument));

            document.setStatus(DocumentStatus.SIGNED);
            policyDocumentService.savePolicyDocument(document);

            logger.info("Signature saved and emails sent successfully for document ID: " + id);

            return ResponseEntity.ok("Signature saved and emails sent successfully");

        } catch (IOException | MessagingException e) {
            logger.severe("Error saving signature or sending emails: " + e.getMessage());
            return ResponseEntity.status(500).body("Error saving signature or sending emails");
        }
    }

    private byte[] signDocument(PolicyDocument document, SignatureRequest signatureRequest) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("policysign")
                .key(document.getUrl())
                .build();
    
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        PDDocument pdfDocument = PDDocument.load(s3Object);
    
        PDPage page = pdfDocument.getPage(document.getSignaturePage()); // Ensure this is correct
    
        // Decode base64 signature image
        String base64Image = signatureRequest.getSignatureDataUrl().replace("data:image/png;base64,", "");
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        InputStream imageInputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(imageInputStream);
    
        // Create PDImageXObject from BufferedImage
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdfDocument, imageBytes, "signature");
    
        // Calculate the image height to maintain aspect ratio
        float imageWidth = document.getSignatureWidth();
        float imageHeight = (imageWidth / bufferedImage.getWidth()) * bufferedImage.getHeight();
    
        // Draw the image onto the PDF
        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page, PDPageContentStream.AppendMode.APPEND, true);
        contentStream.drawImage(pdImage, document.getXSignature(), 842 - document.getYSignature(), imageWidth, imageHeight);
        contentStream.close();
    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdfDocument.save(outputStream);
        pdfDocument.close();
    
        return outputStream.toByteArray();
    }

    private void sendEmail(String recipientEmail, byte[] document) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject("Document Signed");
        helper.setText("The document has been signed successfully. Please find the attached signed document.");

        helper.addAttachment("signed_document.pdf", new ByteArrayResource(document));

        mailSender.send(message);
    }
}
