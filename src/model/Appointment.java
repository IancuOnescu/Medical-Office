package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data @AllArgsConstructor
abstract public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private Date date;

    public Appointment(){}

    @Override
    public int hashCode() {
        return Objects.hash(patient, doctor, date);
    }
}
