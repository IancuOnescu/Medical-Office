package model;

import lombok.Data;

import java.util.Date;

@Data
public class Refferal extends Document {
    private Office office;
    private Date appointmentDate;
    private Doctor doctor;

    public Refferal(Date date, Doctor doctor, String[] args){
        super(date, doctor);
    }

    public String getArgs(){
        return office.getName() + "/" + appointmentDate.toString() + "/" + doctor.getFirstName() + " " + doctor.getLastName();
    }
}
