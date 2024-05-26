package ch.zhaw.policysign.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.policysign.model.Signatory;
import ch.zhaw.policysign.repository.SignatoryRepository;

public class SignatoryServiceTest {

    @InjectMocks
    private SignatoryService signatoryService;

    @Mock
    private SignatoryRepository signatoryRepository;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveSignatory() {
        Signatory signatory = new Signatory();
        when(signatoryRepository.save(signatory)).thenReturn(signatory);
        
        Signatory savedSignatory = signatoryService.saveSignatory(signatory);

        assertEquals(signatory, savedSignatory);
        verify(signatoryRepository, times(1)).save(signatory);
    }
        
}
