package model;

import lombok.Data;
import java.util.Date;
import java.util.Objects;

@Data
public class Certificate extends Document{
    private Patient patient;
    private String description;

    public Certificate(Date date, Doctor doctor, String[] args){
        super(date, doctor);
        this.patient = patient;
        this.description = description;
    }

    public String getArgs(){
        return patient.toString() + "/" + description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getDoctor(), super.getDate());
    }
}
