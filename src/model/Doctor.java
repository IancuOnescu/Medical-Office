package model;

import lombok.Data;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;

@Data
public class Doctor extends Entity{
    private TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getDate));
    private HashSet<Document> signedDocuments = new HashSet<Document>();
    private int noOffice;

    public Doctor(){}

    @Override
    public int hashCode() {
        return Objects.hash(super.getLastName(), super.getFirstName());
    }
}
