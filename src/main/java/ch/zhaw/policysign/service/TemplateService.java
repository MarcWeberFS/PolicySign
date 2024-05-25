package ch.zhaw.policysign.service;

import ch.zhaw.policysign.model.Template;
import ch.zhaw.policysign.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;

    public Template saveTemplate(Template template) {
        return templateRepository.save(template);
    }

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Optional<Template> getTemplateById(String id) {
        return templateRepository.findById(id);
    }

    public void deleteTemplate(String id) {
        templateRepository.deleteById(id);
    }
}
