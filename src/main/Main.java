package main;

import model.*;

import java.util.*;
import service.*;

public class Main {
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static void main(String[] args){

        //Prezentarea primelor 10 actiuni/interogari
        Doctor doc1 = new Doctor();
        doc1.setLastName("Miguel");
        Doctor doc2 = new Doctor();
        doc2.setLastName("Fernando");


        Patient patient = new Patient();
        Date dt = new Date();
        Date dtt = addDays(dt, 10);

        Office off = new Office();
        OfficeService os = new OfficeService(off);

        os.hireDoctor(doc1);
        os.hireDoctor(doc2);

        os.makeAppointment(patient, doc1, dtt, "Colonoscopy");
        os.makeAppointment(patient, doc1, dt, "Endoscopy"); // To prove that the set is ordered by date
        os.makeAppointment(patient, doc2, dt, "Auscultation");
        os.makeAppointment(patient, doc2, dtt, "Examination");

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

        System.out.println(doc1.getSignedDocuments().toString());

        System.out.println("-----------------------------------------------");

        os.transferAppointment(doc2, doc1, patient, dtt);
        System.out.println(doc1.getAppointments());
        System.out.println(doc2.getAppointments());

        System.out.println("-----------------------------------------------");

        os.removeDoctor(doc1);
        System.out.println(off.getDoctors());

        System.out.println("-----------------------------------------------");

        System.out.println(os.availableDoctors(dtt));
    }
}
