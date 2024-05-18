package main.java.ch.zhaw.policysign.model;

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
public class Signatory {
    
    private boolean signed;
    private Date signDate;
}
