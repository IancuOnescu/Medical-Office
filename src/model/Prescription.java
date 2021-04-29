package model;

import lombok.Data;

import java.util.Date;

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
}
