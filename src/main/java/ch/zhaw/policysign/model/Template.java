package ch.zhaw.policysign.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "templates")
public class Template {
    @Id
    private String id;
    private String url;
    private float xSignature;
    private float ySignature;
    private float signatureWidth;
    private String userId;
    private String title;
    private String description;
}
