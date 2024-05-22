package ch.zhaw.policysign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ch.zhaw.policysign.model.Signatory;
import ch.zhaw.policysign.repository.SignatoryRepository;

import java.util.Optional;

@Service
public class SignatoryService {

    @Autowired
    private SignatoryRepository signatoryRepository;

    public Signatory saveSignatory(Signatory signatory) {
        return signatoryRepository.save(signatory);
    }

}
