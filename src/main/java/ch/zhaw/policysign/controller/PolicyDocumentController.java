package ch.zhaw.policysign.controller;

import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.service.PolicyDocumentService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class PolicyDocumentController {

    @Autowired
    private PolicyDocumentService policyDocumentService;

    @PostMapping
    public PolicyDocument submitPolicyDocument(@RequestBody PolicyDocument policyDocument) {
        policyDocument.setCreationDate(new Date());
        policyDocument.setUpdateDate(new Date());
        return policyDocumentService.savePolicyDocument(policyDocument);
    }
}
