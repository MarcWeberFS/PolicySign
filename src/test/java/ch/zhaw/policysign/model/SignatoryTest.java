package ch.zhaw.policysign.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.Test;

public class SignatoryTest {

    @Test
    public void testSignatoryAllArgsConstructor() {
        Date now = new Date(0,0,0);
        Signatory signatory = new Signatory("1", true, now, "docId", "dataUrl");

        assertEquals("1", signatory.getId());
        assertTrue(signatory.isSigned());
        assertEquals(now, signatory.getSignDate());
        assertEquals("docId", signatory.getDocumentId());
        assertEquals("dataUrl", signatory.getSignatureDataUrl());
    }


    @Test
    public void testSignatorySettersAndGetters() {
        Signatory signatory = new Signatory();
        signatory.setId("123");
        signatory.setSigned(true);
        Date now = new Date(0, 0, 0);
        signatory.setSignDate(now);
        signatory.setDocumentId("docId");

        assertEquals("123", signatory.getId());
        assertTrue(signatory.isSigned());
        assertEquals(now, signatory.getSignDate());
        assertEquals("docId", signatory.getDocumentId());

    }
}
