package model;

import lombok.Data;

import java.util.Date;

@Data
public class Auscultation extends Test{
    private final Double cost = 99.99;

    public Auscultation(String type, Patient patient, Doctor doctor, Date date){
        super(type, patient, doctor, date);
    }
}
