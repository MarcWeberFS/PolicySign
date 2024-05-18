package ch.zhaw.policysign.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.policysign.model.User;

public interface UserRepository extends MongoRepository<User, String>{
    
}
