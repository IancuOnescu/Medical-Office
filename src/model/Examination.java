package model;

import lombok.Data;

import java.util.Date;

@Data
public class Examination extends Appointment{
    private final Integer cost = 200;

    public Examination(Patient patient, Doctor doctor, Date date){
        super(patient, doctor, date);
    }
}
