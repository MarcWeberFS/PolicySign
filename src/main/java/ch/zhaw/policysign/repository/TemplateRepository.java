package ch.zhaw.policysign.repository;

import ch.zhaw.policysign.model.Template;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateRepository extends MongoRepository<Template, String> {
    List<Template> findByUserId(String userId);
}
