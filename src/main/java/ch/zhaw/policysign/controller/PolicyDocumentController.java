package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.DocumentStatus;
import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.service.PolicyDocumentService;
import ch.zhaw.policysign.service.EmailService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class PolicyDocumentController {

    @Autowired
    private PolicyDocumentService policyDocumentService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public PolicyDocument submitPolicyDocument(@RequestBody PolicyDocument policyDocument) throws MessagingException {
        policyDocument.setCreationDate(new Date());
        policyDocument.setUpdateDate(new Date());
        policyDocument.setStatus(DocumentStatus.PENDING);
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
}
