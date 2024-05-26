package ch.zhaw.policysign.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private List<String> roles = List.of("user");
}
