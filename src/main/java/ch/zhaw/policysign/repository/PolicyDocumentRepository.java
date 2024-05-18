package ch.zhaw.policysign.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.policysign.model.PolicyDocument;

public interface PolicyDocumentRepository extends MongoRepository<PolicyDocument, String>{
    
}
