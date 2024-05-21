package ch.zhaw.policysign.service;

import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.repository.PolicyDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolicyDocumentService {
    @Autowired
    private PolicyDocumentRepository policyDocumentRepository;

    public PolicyDocument savePolicyDocument(PolicyDocument policyDocument) {
        return policyDocumentRepository.save(policyDocument);
    }
}
