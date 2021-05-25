package model;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class Refferal extends Document {
    private Office office;
    private Date appointmentDate;
    private Doctor toDoctor;

    public Refferal(Date date, Doctor doctor, String[] args){
        super(date, doctor);
    }

    public String getArgs(){
        return office.getName() + "/" + appointmentDate.toString() + "/" + toDoctor.getFirstName() + " " + toDoctor.getLastName();
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.getDoctor(), super.getDate());
    }
}
