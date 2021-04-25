package model;

import lombok.Data;

import java.util.Date;

@Data
public class Auscultation extends Test{
    private Double temperature;

    public Auscultation(Patient patient, Doctor doctor, Date date, Integer noReservedMachine, Double temperature){
        super(patient, doctor, date, 100.0, noReservedMachine);
        this.temperature = temperature;
    }
}
