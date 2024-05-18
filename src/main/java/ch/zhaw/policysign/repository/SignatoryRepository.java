package ch.zhaw.policysign.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.policysign.model.Signatory;

public interface SignatoryRepository extends MongoRepository<Signatory, String>{
    
}
