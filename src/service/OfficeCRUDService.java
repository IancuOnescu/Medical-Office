package service;

import config.DatabaseConnection;
import model.*;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

public class OfficeCRUDService {
    private final Office office;
    private final LoggingService logService = new LoggingService();

    public OfficeCRUDService(Office office) {
        this.office = office;
    }

    public Boolean isAvailable(@NotNull Doctor doctor, @NotNull Date date){
        TreeSet<Appointment> appointments = doctor.getAppointments();

        for(Appointment app : appointments)
            if (app.getDate().equals(date))
                return false;

        logService.logAction("isAvailable");
        return true;
    }

    public void makeAppointment(@NotNull Patient patient, @NotNull Doctor doctor, @NotNull Date date, String type, String[] args){ //true if doctor is available
        if(!isAvailable(doctor, date)) {
            System.out.println("Ne pare rau, doctorul " + doctor.getLastName() + "nu este disponibil!");
            return;
        }
        TreeSet<Appointment> appointments = doctor.getAppointments();
        Appointment ap;

        switch (type) {
            case "examination":
                ap = new Examination(patient, doctor, date, args[0]); break;
            case "endoscopy":
                ap = new Endoscopy(patient, doctor, date, Integer.parseInt(args[0]), args[1]); break;
            case "colonoscopy":
                ap = new Colonoscopy(patient, doctor, date, Integer.parseInt(args[0]), args[1]); break;
            case "auscultation":
                ap = new Auscultation(patient, doctor, date, Integer.parseInt(args[0]), Double.parseDouble(args[1])); break;
            default: return;
        }
        appointments.add(ap);

        String sql = "insert into medical_office.appointment values (?, ?, ?, ?, ?, ?, ?) ";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {//try with resources
            statement.setInt(1, ap.hashCode());
            statement.setInt(2, ap.getPatient().hashCode());
            statement.setInt(3, ap.getDoctor().hashCode());
            statement.setDate(4, new java.sql.Date(ap.getDate().getTime()));
            statement.setDouble(5, ap.getCost());
            statement.setString(6, type);
            statement.setString(7, args[1]);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        logService.logAction("makeAppointment");
    }

    public void cancelAppointment(@NotNull Patient patient, @NotNull Doctor doctor, @NotNull Date date){
        TreeSet<Appointment> appointments = doctor.getAppointments();

        String sql = "delete from medical_office.appointment where id = ?";

        for(Appointment ap : appointments)
            if(ap.getDate().equals(date) && ap.getPatient().equals(patient)){
                appointments.remove(ap);
                try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
                    statement.setInt(1, ap.hashCode());
                    statement.executeUpdate();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                return;
            }

        logService.logAction("cancelAppointment");
    }

    public void transferAppointment(Doctor fromDoctor, Doctor toDoctor, Patient patient, Date date){
        TreeSet<Appointment> toApointments = toDoctor.getAppointments();
        TreeSet<Appointment> fromApointments = fromDoctor.getAppointments();

        String sql = "update medical_office.appointment set doctor_id = ? where id = ?";

        for(Appointment ap : fromApointments)
            if(ap.getDate().equals(date) && ap.getPatient().equals(patient)){
                ap.setDoctor(toDoctor);
                fromApointments.remove(ap);
                toApointments.add(ap);

                try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
                    statement.setInt(1, toDoctor.hashCode());
                    statement.setInt(2, ap.hashCode());
                    statement.executeUpdate();
                } catch(SQLException e) {
                    e.printStackTrace();
                }

                return;
            }

        logService.logAction("transferAppointment");
    }

    public void hireDoctor(Doctor doctor){
        HashSet<Doctor> docs = this.office.getDoctors();
        docs.add(doctor);

        String sql = "insert into medical_office.doctor values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {//try with resources
            statement.setInt(1, doctor.hashCode());
            statement.setString(2, doctor.getFirstName());
            statement.setString(3, doctor.getLastName());
            statement.setString(4, doctor.getCnp());
            statement.setString(5, Arrays.toString(doctor.getPhoneNumber()).replaceAll("\\[|\\]|,|\\s", ""));
            statement.setInt(6, doctor.getNoOffice());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        logService.logAction("hireDoctor");
    }

    public void removeDoctor(Doctor doctor){
        HashSet<Doctor> docs = this.office.getDoctors();
        docs.remove(doctor);

        String sql = "delete from medical_office.doctor where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {//try with resources
            statement.setInt(1, doctor.hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        logService.logAction("removeDoctor");
    }

    public void updateDoctor(Doctor doctor){
        // ATTENTION: if the new doctor object has a different first name, last name or cnp
        // this will not work for the hashcode will be different
        // and it will invalidate all the tables where the doctor_id was used
        String sql = "update medical_office.doctor set phoneNumber = ?, noOffice = ? where id = ?";

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {//try with resources
            statement.setString(1, Arrays.toString(doctor.getPhoneNumber()).replaceAll("\\[|\\]|,|\\s", ""));
            statement.setInt(2, doctor.getNoOffice());
            statement.setInt(3, doctor.hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPatient(Patient patient){
        String sql = "insert into medical_office.patient values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {//try with resources
            statement.setInt(1, patient.hashCode());
            statement.setString(2, patient.getFirstName());
            statement.setString(3, patient.getLastName());
            statement.setString(4, patient.getCnp());
            statement.setString(5, Arrays.toString(patient.getPhoneNumber()).replaceAll("\\[|\\]|,|\\s", ""));
            statement.setInt(6, patient.getFamilyDoctor().hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePatient(Patient patient){
        String sql = "delete from medical_office.patient where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, patient.hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient(Patient patient){
        String sql = "update medical_office.patient set phoneNumber = ?, doctor_id = ? where id = ?";

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {//try with resources
            statement.setString(1, Arrays.toString(patient.getPhoneNumber()).replaceAll("\\[|\\]|,|\\s", ""));
            statement.setInt(2, patient.getFamilyDoctor().hashCode());
            statement.setInt(3, patient.hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void addRefferal(Refferal ref){
        String sql = "insert into medical_office.refferal values (?, ?, ?, ?, ?)";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, ref.hashCode());
            statement.setInt(2, ref.getToDoctor().hashCode());
            statement.setInt(3, ref.getDoctor().hashCode());
            statement.setDate(4, new java.sql.Date(ref.getDate().getTime()));
            statement.setInt(5, ref.getOffice().hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCertificate(Certificate cert){
        String sql = "insert into medical_office.certificate values (?, ?, ?, ?, ?)";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, cert.hashCode());
            statement.setInt(2, cert.getPatient().hashCode());
            statement.setInt(3, cert.getDoctor().hashCode());
            statement.setDate(4, new java.sql.Date(cert.getDate().getTime()));
            statement.setString(5, cert.getDescription());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPrescription(Prescription pres){
        String sql = "insert into medical_office.prescription values (?, ?, ?, ?, ?)";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, pres.hashCode());
            statement.setInt(2, pres.getPatient().hashCode());
            statement.setInt(3, pres.getDoctor().hashCode());
            statement.setDate(4, new java.sql.Date(pres.getDate().getTime()));
            statement.setString(5, String.join("/", pres.getMeds()));
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDocument(Document doc){
        if(doc instanceof Refferal)
            addRefferal((Refferal) doc);
        else if(doc instanceof Certificate)
            addCertificate((Certificate) doc);
        else
            addPrescription((Prescription) doc);

        doc.getDoctor().getSignedDocuments().add(doc);
    }

    private void updateRefferal(Refferal ref){
        String sql = "update medical_office.refferal set to_doctor_id = ?, office = ? where id = ?";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(3, ref.hashCode());
            statement.setInt(1, ref.getToDoctor().hashCode());
            statement.setInt(2, ref.getOffice().hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCertificate(Certificate cert){
        String sql = "update medical_office.certificate set patient_id = ?, description = ? where id = ?";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(3, cert.hashCode());
            statement.setInt(1, cert.getPatient().hashCode());
            statement.setString(2, cert.getDescription());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePrescription(Prescription pres){
        String sql = "update medical_office.prescription set patient_id = ?, meds = ? where id = ?";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(3, pres.hashCode());
            statement.setInt(1, pres.getPatient().hashCode());
            statement.setString(2, String.join("/", pres.getMeds()));
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDocument(Document doc){
        if(doc instanceof Refferal)
            updateRefferal((Refferal) doc);
        else if(doc instanceof Certificate)
            updateCertificate((Certificate) doc);
        else
            updatePrescription((Prescription) doc);
    }

    private void deleteRefferal(Refferal ref){
        String sql = "delete from medical_office.refferal where id = ?";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, ref.hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCertificate(Certificate cert){
        String sql = "delete from medical_office.certificate where id = ?";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, cert.hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        System.out.println(cert.hashCode());
    }

    private void deletePrescription(Prescription pres){
        String sql = "delete from medical_office.prescription where id = ?";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, pres.hashCode());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDocument(Document doc) {
        if (doc instanceof Refferal)
            deleteRefferal((Refferal) doc);
        else if (doc instanceof Certificate)
            deleteCertificate((Certificate) doc);
        else
            deletePrescription((Prescription) doc);

        doc.getDoctor().getSignedDocuments().remove(doc);
    }
}
