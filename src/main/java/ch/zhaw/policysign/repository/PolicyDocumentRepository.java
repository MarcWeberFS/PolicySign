package ch.zhaw.policysign.repository;

import ch.zhaw.policysign.model.PolicyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PolicyDocumentRepository extends MongoRepository<PolicyDocument, String> {
}
