package model;

import lombok.Data;

import java.util.Date;

@Data
public class Endoscopy extends Test{
    private final Double cost = 250.0;

    public Endoscopy(String type, Patient patient, Doctor doctor, Date date){
        super(type, patient, doctor, date);
    }
}
