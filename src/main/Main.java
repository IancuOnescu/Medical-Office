package main;

import model.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import service.*;

import javax.print.Doc;

public class Main {
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static void main(String[] args) throws IOException, ParseException {

        //Prezentarea primelor 10 actiuni/interogari
        /*Doctor doc1 = new Doctor();
        doc1.setLastName("Miguel");
        doc1.setFirstName("Miguelito");
        Doctor doc2 = new Doctor();
        doc2.setLastName("Fernando");
        doc2.setFirstName("Fernandoinho");


        Patient patient = new Patient(doc1);
        patient.setLastName("Mario");
        patient.setFirstName("Luigi");
        patient.setCnp("5210409448407");
        Date dt = new Date();
        Date dtt = addDays(dt, 10);

        Office off = new Office();
        OfficeService os = new OfficeService(off);

        os.hireDoctor(doc1);
        os.hireDoctor(doc2);

        os.makeAppointment(patient, doc1, dtt, "Colonoscopy", new String[] {"441", "Perodixyl"});
        os.makeAppointment(patient, doc1, dt, "Endoscopy", new String[] {"221", "Tetraoxycilin"}); ; // To prove that the set is ordered by date
        os.makeAppointment(patient, doc2, dt, "Auscultation", new String[] {"112", "37.8"});
        os.makeAppointment(patient, doc2, dtt, "Examination", new String[] {"general examination"});

        System.out.println(doc1.getAppointments());
        os.cancelAppointment(patient, doc1, dtt);
        System.out.println(doc1.getAppointments());

        System.out.println("-----------------------------------------------");

        System.out.println(os.getDoctorAgenda(doc2, dtt));

        System.out.println("-----------------------------------------------");

        System.out.println(os.getPatientHistory(patient));

        System.out.println("-----------------------------------------------");

        String description = "Subsemnatul este apt pentru educatie fizica!";
        os.signCertificate(doc1, patient, dt, description);

        System.out.println(doc1.getSignedDocuments());

        System.out.println("-----------------------------------------------");

        os.transferAppointment(doc2, doc1, patient, dtt);
        System.out.println(doc1.getAppointments());
        System.out.println(doc2.getAppointments());

        System.out.println("-----------------------------------------------");

        os.removeDoctor(doc1);
        System.out.println(off.getDoctors());

        System.out.println("-----------------------------------------------");

        System.out.println(os.availableDoctors(dtt));*/


        ///Testing new stuff TO DO: Design tweaks and correct placement of WriterService
        Office off = new Office();

        OfficeService os = new OfficeService(off);
        FileReaderService svc = FileReaderService.FileReaderService();
        FileWriterService fws = FileWriterService.FileWriterService();

        HashSet<Patient> pats = svc.loadPatients();

        Doctor doc1 = new Doctor("Oliver1", "Erfan1","5150015079800", new Integer[] {0, 7, 1, 2, 3, 1, 2, 3, 1, 2},112);
        os.hireDoctor(doc1);

        Patient patient = new Patient("John1", "Travolta1" ,"4510416016541", new Integer[] {0, 7, 1, 2, 3, 1, 2, 3, 1, 2});
        System.out.println(patient);
        pats.add(patient);
        pats.forEach(System.out :: println);
        fws.writePatients(pats);

        Date dt = new Date();
        String description = "Subsemnatul este apt pentru educatie fizica!";
        HashSet<Document> documents = new HashSet<>();
        Certificate cert = new Certificate(dt, doc1, new String[]{patient.getFirstName(), description});
        cert.setPatient(patient);
        documents.add(cert);
        fws.writeDocuments(documents);

        HashSet<Doctor> docs = off.getDoctors();
        for(Doctor doc : docs)
            if(doc.getLastName().equals("Erfan"))
                fws.writeAppointments(doc.getAppointments());
    }
}
