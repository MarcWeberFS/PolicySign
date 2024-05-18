package ch.zhaw.policysign.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "policysign")
public class PolicyDocument {

    @Id
    private String id;
    private String title;
    private String description;
    private String documentBase64;
    private DocumentStatus status = DocumentStatus.PENDING;
    private Date creationDate;
    private Date updateDate;
    private int xSignature;
    private int ySignature;
    private int signatureWidth;
    private int signaturePage;
    private String userId;
    private String signedByEmail;
}
