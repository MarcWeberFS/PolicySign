package ch.zhaw.policysign.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ch.zhaw.policysign.model.Signatory;

import java.util.Optional;

public interface SignatoryRepository extends MongoRepository<Signatory, String> {
    Optional<Signatory> findByDocumentId(String documentId);
}
