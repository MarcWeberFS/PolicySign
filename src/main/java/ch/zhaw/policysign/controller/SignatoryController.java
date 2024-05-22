package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.model.Signatory;
import ch.zhaw.policysign.service.PolicyDocumentService;
import ch.zhaw.policysign.service.SignatoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Logger;

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
    private S3Client s3Client;

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
        String fileKey = document.getUrl(); // Retrieve the key

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
    public ResponseEntity<String> saveSignature(@PathVariable String id, @RequestBody String signatureDataUrl) {
        logger.info("Saving signature for document ID: " + id);

        Optional<PolicyDocument> documentOpt = Optional.of(policyDocumentService.getPolicyDocumentById(id));
        if (documentOpt.isEmpty()) {
            logger.warning("Policy document not found for document ID: " + id);
            return ResponseEntity.notFound().build();
        }

        Signatory signatory = new Signatory();
        signatory.setDocumentId(id);
        signatory.setSigned(true);
        signatory.setSignDate(new java.util.Date());
        signatory.setSignatureDataUrl(signatureDataUrl);
        signatoryService.saveSignatory(signatory);

        logger.info("Signature saved successfully for document ID: " + id);

        return ResponseEntity.ok("Signature saved successfully");
    }
}
