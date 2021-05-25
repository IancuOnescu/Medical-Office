package model;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class Prescription extends Document{
    private String[] meds;
    private Patient patient;

    public Prescription(Date date, Doctor doctor, String[] args){
        super(date, doctor);
    }
    public String getArgs(){
        return String.join("/", meds) + "/" + patient.toString();
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.getDoctor(), super.getDate());
    }
}
