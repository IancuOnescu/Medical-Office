package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public abstract class Test extends Appointment{
    private String type;

    public Test(){}

    public Test(String type, Patient patient, Doctor doctor, Date date){
        super(patient, doctor, date);
        this.type = type;
    }
}
