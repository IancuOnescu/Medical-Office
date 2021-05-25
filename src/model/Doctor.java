package model;

import lombok.Data;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;

@Data
public class Doctor extends Entity{
    private TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getDate));
    private HashSet<Document> signedDocuments = new HashSet<>();
    private int noOffice;

    public Doctor(){}
    public Doctor(String firstName, String lastName, String CNP, Integer[] phoneNumber, int noOffice) {
        super(firstName, lastName, CNP, phoneNumber);
        this.noOffice = noOffice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLastName(), super.getFirstName(), super.getCnp());
    }
    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        final Doctor app = (Doctor) obj;
        return this.appointments.equals(app.getAppointments()) && this.signedDocuments.equals(app.getSignedDocuments()) && super.getLastName().equals(app.getLastName()) && super.getFirstName().equals((app.getFirstName()));
    }
    @Override
    public String toString() {
        return super.getLastName() + " " + super.getFirstName() + " office number: " + noOffice;
    }
}
