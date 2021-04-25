package model;

import lombok.Data;

import java.util.Date;

@Data
public class Examination extends Appointment{
    private String type;

    public Examination(Patient patient, Doctor doctor, Date date, String type){
        super(patient, doctor, date, 100.0);
        this.type = type;
    }
}
