package model;

import lombok.Data;

import java.util.Date;

@Data
public class Colonoscopy extends Test{
    private String preparingDrugs;

    public Colonoscopy(Patient patient, Doctor doctor, Date date, Integer noReservedMachine, String preparingDrugs){
        super(patient, doctor, date, 450.0, noReservedMachine);
        this.preparingDrugs = preparingDrugs;
    }

    public String getArgs(){
        return preparingDrugs;
    }
}
