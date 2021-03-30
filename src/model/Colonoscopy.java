package model;

import lombok.Data;

import java.util.Date;

@Data
public class Colonoscopy extends Test{
    private final Double cost = 249.9;

    public Colonoscopy(String type, Patient patient, Doctor doctor, Date date){
        super(type, patient, doctor, date);
    }
}
