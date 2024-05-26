package ch.zhaw.policysign.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TemplateTest {
    @Test
    public void testTemplateSettersAndGetters() {
        Template template = new Template();
        template.setId("123");
        template.setUrl("http://test.com");
        template.setXSignature(100);
        template.setYSignature(200);
        template.setSignatureWidth(50);
        template.setUserId("userId");
        template.setTitle("Test Title");
        template.setDescription("Test Description");

        assertEquals("123", template.getId());
        assertEquals("http://test.com", template.getUrl());
        assertEquals(100, template.getXSignature());
        assertEquals(200, template.getYSignature());
        assertEquals(50, template.getSignatureWidth());
        assertEquals("userId", template.getUserId());
        assertEquals("Test Title", template.getTitle());
        assertEquals("Test Description", template.getDescription());
    }
}
