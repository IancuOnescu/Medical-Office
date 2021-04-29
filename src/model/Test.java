package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public abstract class Test extends Appointment{
    private Integer noReservedMachine;

    public Test(){}

    public Test(Patient patient, Doctor doctor, Date date, Double cost, Integer noReservedMachine){
        super(patient, doctor, date, cost);
        this.noReservedMachine = noReservedMachine;
    }

    public abstract String getArgs();
}
