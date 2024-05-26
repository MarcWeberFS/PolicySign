package ch.zhaw.policysign.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyDocumentTest {
    @Test
    public void testPolicyDocumentAllArgsConstructor() {
        Date now = new Date();
        PolicyDocument document = new PolicyDocument("1", "Title", "Description", "http://example.com", 
                DocumentStatus.PENDING, now, now, 100.0f, 200.0f, 50.0f, 1, "userId", "test@example.com");

        assertEquals("1", document.getId());
        assertEquals("Title", document.getTitle());
        assertEquals("Description", document.getDescription());
        assertEquals("http://example.com", document.getUrl());
        assertEquals(DocumentStatus.PENDING, document.getStatus());
        assertEquals(now, document.getCreationDate());
        assertEquals(now, document.getUpdateDate());
        assertEquals(100.0f, document.getXSignature());
        assertEquals(200.0f, document.getYSignature());
        assertEquals(50.0f, document.getSignatureWidth());
        assertEquals(1, document.getSignaturePage());
        assertEquals("userId", document.getUserId());
        assertEquals("test@example.com", document.getSignedByEmail());
    }

    @Test
    public void testPolicyDocumentNoArgsConstructor(){
        PolicyDocument document = new PolicyDocument();

        assertNull(document.getId());
        assertNull(document.getTitle());
        assertNull(document.getDescription());
        assertNull(document.getUrl());
        assertEquals(DocumentStatus.PENDING, document.getStatus());
        assertEquals(0.0f, document.getXSignature());
        assertEquals(0.0f, document.getYSignature());
        assertEquals(0.0f, document.getSignatureWidth());
        assertEquals(0, document.getSignaturePage());
        assertNull(document.getUserId());
        assertNull(document.getSignedByEmail());
    }

    @Test
    public void testPolicyDocumentToString() {
        Date now = new Date();
        PolicyDocument document = new PolicyDocument("1", "Title", "Description", "http://example.com", 
                DocumentStatus.PENDING, now, now, 100.0f, 200.0f, 50.0f, 1, "userId", "test@mail.com");

        String expectedString = "PolicyDocument(id=1, title=Title, description=Description, url=http://example.com, status=PENDING, creationDate=" + now + ", updateDate=" + now + ", xSignature=100.0, ySignature=200.0, signatureWidth=50.0, signaturePage=1, userId=userId, signedByEmail=test@mail.com)";
        assertEquals(expectedString, document.toString());
    }

    @Test
    public void testPolicyDocumentSettersAndGetters() {
        PolicyDocument document = new PolicyDocument();
        document.setId("123");
        document.setTitle("Test Title");
        document.setDescription("Test Description");
        document.setUrl("http://test.com");
        document.setStatus(DocumentStatus.SIGNED);
        Date now = new Date();
        document.setCreationDate(now);
        document.setUpdateDate(now);
        document.setXSignature(100);
        document.setYSignature(200);
        document.setSignatureWidth(50);
        document.setSignaturePage(1);
        document.setUserId("userId");
        document.setSignedByEmail("test@example.com");

        assertEquals("123", document.getId());
        assertEquals("Test Title", document.getTitle());
        assertEquals("Test Description", document.getDescription());
        assertEquals("http://test.com", document.getUrl());
        assertEquals(DocumentStatus.SIGNED, document.getStatus());
        assertEquals(now, document.getCreationDate());
        assertEquals(now, document.getUpdateDate());
        assertEquals(100, document.getXSignature());
        assertEquals(200, document.getYSignature());
        assertEquals(50, document.getSignatureWidth());
        assertEquals(1, document.getSignaturePage());
        assertEquals("userId", document.getUserId());
        assertEquals("test@example.com", document.getSignedByEmail());
    }
}
