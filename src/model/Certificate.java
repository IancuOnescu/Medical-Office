package model;

import lombok.Data;
import java.util.Date;

@Data
public class Certificate extends Document{
    private Patient patient;
    private String description;

    public Certificate(Date date, Doctor doctor, Patient patient, String description){
        super(date, doctor);
        this.patient = patient;
        this.description = description;
    }
}
