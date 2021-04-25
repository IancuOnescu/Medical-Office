package service;

import lombok.Data;
import model.*;
import org.jetbrains.annotations.NotNull;

import javax.print.Doc;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

@Data
public class OfficeService {
    Office office;

    //We spcifically define the param constr so we don't have a blank one
    public OfficeService(Office office){
        this.office = office;
    }

    public TreeSet<Appointment> getDoctorAgenda(Doctor doctor, Date date){
        TreeSet<Appointment> ret = new TreeSet<>(Comparator.comparing(Appointment::getDate));
        TreeSet<Appointment> appointments = doctor.getAppointments();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        for(Appointment ap : appointments)
            if(sdf.format(date).equals(sdf.format(ap.getDate())))
                ret.add(ap);

        return ret;
    }

    public Boolean isAvailable(@NotNull Doctor doctor, @NotNull Date date){
        TreeSet<Appointment> appointments = doctor.getAppointments();

        for(Appointment app : appointments)
            if (app.getDate().equals(date))
                return false;

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
            case "Examination":
                ap = new Examination(patient, doctor, date, args[0]); break;
            case "Endoscopy":
                ap = new Endoscopy(patient, doctor, date, Integer.parseInt(args[0]), args[1]); break;
            case "Colonoscopy":
                ap = new Colonoscopy(patient, doctor, date, Integer.parseInt(args[0]), args[1]); break;
            case "Auscultation":
                ap = new Auscultation(patient, doctor, date, Integer.parseInt(args[0]), Double.parseDouble(args[1])); break;
            default: return;
        }
        appointments.add(ap);
    }

    public void cancelAppointment(@NotNull Patient patient, @NotNull Doctor doctor, @NotNull Date date){
        TreeSet<Appointment> appointments = doctor.getAppointments();

        for(Appointment ap : appointments)
            if(ap.getDate().equals(date) && ap.getPatient().equals(patient)){
                appointments.remove(ap);
                return;
            }

    }

    public HashSet<Appointment> getPatientHistory(@NotNull Patient patient){
        HashSet<Appointment> ret = new HashSet<>();
        HashSet<Doctor> doctors = this.office.getDoctors();

        for(Doctor doc : doctors){
            TreeSet<Appointment> appointments = doc.getAppointments();

            for(Appointment app : appointments)
                if(app.getPatient().equals(patient))
                    ret.add(app);
        }

        return ret;
    }

    public void signCertificate(Doctor doctor, Patient patient, Date date, String description){
        Certificate cert = new Certificate(date, doctor, patient, description);

        HashSet<Document> rel = doctor.getSignedDocuments();
        rel.add(cert);
    }

    public void hireDoctor(Doctor doctor){
        HashSet<Doctor> docs = this.office.getDoctors();
        docs.add(doctor);
    }

    public void removeDoctor(Doctor doctor){
        HashSet<Doctor> docs = this.office.getDoctors();
        docs.remove(doctor);
    }

    public void transferAppointment(Doctor fromDoctor, Doctor toDoctor, Patient patient, Date date){
        TreeSet<Appointment> toApointments = toDoctor.getAppointments();
        TreeSet<Appointment> fromApointments = fromDoctor.getAppointments();

        for(Appointment ap : fromApointments)
            if(ap.getDate().equals(date) && ap.getPatient().equals(patient)){
                ap.setDoctor(toDoctor);
                fromApointments.remove(ap);
                toApointments.add(ap);
                return;
            }
    }

    public HashSet<Doctor> availableDoctors(Date date){
        HashSet<Doctor> doctors = this.office.getDoctors();
        HashSet<Doctor> ret = new HashSet<>();

        for(Doctor doc : doctors)
            if(isAvailable(doc, date))
                ret.add(doc);

        return ret;
    }
}
