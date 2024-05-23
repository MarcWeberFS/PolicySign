package ch.zhaw.policysign.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import ch.zhaw.policysign.model.PolicyDocument;

public interface PolicyDocumentRepository extends MongoRepository<PolicyDocument, String> {
    Optional<PolicyDocument> findById(String id);
    List<PolicyDocument> findByUserId(String userId);
}
