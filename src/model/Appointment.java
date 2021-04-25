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
    private Double cost;

    public Appointment(){}

    @Override
    public int hashCode() {
        return Objects.hash(patient, doctor, date, cost);
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        final Appointment app = (Appointment) obj;
        return this.patient.equals(app.getPatient()) && this.doctor.equals(app.getDoctor()) && this.date.equals(app.getDate()) && this.cost.equals(app.getCost());
    }
}
