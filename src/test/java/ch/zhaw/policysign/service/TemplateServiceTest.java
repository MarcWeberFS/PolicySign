package ch.zhaw.policysign.service;

import ch.zhaw.policysign.model.Template;
import ch.zhaw.policysign.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class TemplateServiceTest {

    @InjectMocks
    private TemplateService templateService;

    @Mock
    private TemplateRepository templateRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTemplate() {
        Template template = new Template();
        when(templateRepository.save(template)).thenReturn(template);

        Template savedTemplate = templateService.saveTemplate(template);

        assertEquals(template, savedTemplate);
        verify(templateRepository, times(1)).save(template);
    }

    @Test
    public void testGetAllTemplates() {
        List<Template> templates = new ArrayList<>();
        templates.add(new Template());
        when(templateRepository.findAll()).thenReturn(templates);

        List<Template> result = templateService.getAllTemplates();

        assertEquals(templates, result);
        verify(templateRepository, times(1)).findAll();
    }

    @Test
    public void testGetTemplateById() {
        String id = "123";
        Template template = new Template();
        when(templateRepository.findById(id)).thenReturn(Optional.of(template));

        Optional<Template> result = templateService.getTemplateById(id);

        assertEquals(Optional.of(template), result);
        verify(templateRepository, times(1)).findById(id);
    }

    @Test
    public void testGetTemplateByIdNotFound() {
        String id = "123";
        when(templateRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Template> result = templateService.getTemplateById(id);

        assertEquals(Optional.empty(), result);
        verify(templateRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteTemplate() {
        String id = "123";

        templateService.deleteTemplate(id);

        verify(templateRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetTemplatesByUserId() {
        String userId = "user123";
        List<Template> templates = new ArrayList<>();
        templates.add(new Template());
        when(templateRepository.findByUserId(userId)).thenReturn(templates);

        List<Template> result = templateService.getTemplatesByUserId(userId);

        assertEquals(templates, result);
        verify(templateRepository, times(1)).findByUserId(userId);
    }
}
