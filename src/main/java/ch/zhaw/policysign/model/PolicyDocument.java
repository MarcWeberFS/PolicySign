package ch.zhaw.policysign.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "policysign")
public class PolicyDocument {

    @Id
    private String id;
    private String title;
    private String description;
    private String url;
    private DocumentStatus status = DocumentStatus.PENDING;
    private Date creationDate = new Date();
    private Date updateDate = new Date();
    private float xSignature;
    private float ySignature;
    private float signatureWidth;
    private int signaturePage;
    private String userId;
    private String signedByEmail;
}
