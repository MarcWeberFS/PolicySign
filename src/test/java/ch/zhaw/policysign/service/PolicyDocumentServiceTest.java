package ch.zhaw.policysign.service;

import ch.zhaw.policysign.model.PolicyDocument;
import ch.zhaw.policysign.repository.PolicyDocumentRepository;
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

public class PolicyDocumentServiceTest {

    @InjectMocks
    private PolicyDocumentService policyDocumentService;

    @Mock
    private PolicyDocumentRepository policyDocumentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSavePolicyDocument() {
        PolicyDocument document = new PolicyDocument();
        when(policyDocumentRepository.save(document)).thenReturn(document);

        PolicyDocument savedDocument = policyDocumentService.savePolicyDocument(document);

        assertEquals(document, savedDocument);
        verify(policyDocumentRepository, times(1)).save(document);
    }

    @Test
    public void testGetAllPolicyDocuments() {
        List<PolicyDocument> documents = new ArrayList<>();
        documents.add(new PolicyDocument());
        when(policyDocumentRepository.findAll()).thenReturn(documents);

        List<PolicyDocument> result = policyDocumentService.getAllPolicyDocuments();

        assertEquals(documents, result);
        verify(policyDocumentRepository, times(1)).findAll();
    }

    @Test
    public void testGetPolicyDocumentById() {
        String id = "123";
        PolicyDocument document = new PolicyDocument();
        when(policyDocumentRepository.findById(id)).thenReturn(Optional.of(document));

        PolicyDocument result = policyDocumentService.getPolicyDocumentById(id);

        assertEquals(document, result);
        verify(policyDocumentRepository, times(1)).findById(id);
    }

    @Test
    public void testGetPolicyDocumentByIdNotFound() {
        String id = "123";
        when(policyDocumentRepository.findById(id)).thenReturn(Optional.empty());

        PolicyDocument result = policyDocumentService.getPolicyDocumentById(id);

        assertNull(result);
        verify(policyDocumentRepository, times(1)).findById(id);
    }

    @Test
    public void testGetDocumentsByUserId() {
        String userId = "user123";
        List<PolicyDocument> documents = new ArrayList<>();
        documents.add(new PolicyDocument());
        when(policyDocumentRepository.findByUserId(userId)).thenReturn(documents);

        List<PolicyDocument> result = policyDocumentService.getDocumentsByUserId(userId);

        assertEquals(documents, result);
        verify(policyDocumentRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetPolicyDocumentCount() {
        long count = 10L;
        when(policyDocumentRepository.count()).thenReturn(count);

        long result = policyDocumentService.getPolicyDocumentCount();

        assertEquals(count, result);
        verify(policyDocumentRepository, times(1)).count();
    }

    @Test
    public void testDeletePolicyDocument() {
        String id = "123";

        policyDocumentService.deletePolicyDocument(id);

        verify(policyDocumentRepository, times(1)).deleteById(id);
    }
}
