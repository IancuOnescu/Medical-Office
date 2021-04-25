package model;

import lombok.Data;

import java.util.Date;

@Data
public class Endoscopy extends Test{
    private String sedative;

    public Endoscopy(Patient patient, Doctor doctor, Date date, Integer noReservedMachine, String sedative){
        super(patient, doctor, date, 250.0, noReservedMachine);
        this.sedative = sedative;
    }
}
