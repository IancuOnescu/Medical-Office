package service;

import model.*;

import javax.print.Doc;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileWriterService {
    private static FileWriterService instance = null;
    private FileReaderService fls = FileReaderService.FileReaderService();

    private static final String DIR_PATH = "files";

    private FileWriterService(){}

    public void writeDoctors(Office office){
        HashSet<Doctor> docs = office.getDoctors();

        File file = new File(DIR_PATH + "/Doctors1.csv");
        try {
            FileWriter output = new FileWriter(file);

            String[] header = { "First Name", "Last Name", "CNP", "Phone Number" , "Office number"};
            output.append(String.join(",", header));
            output.append("\n");

            for(Doctor doc : docs) {
                output.append(String.join(",", new String[] {doc.getFirstName(), doc.getLastName(), doc.getCnp(), java.util.Arrays.toString(doc.getPhoneNumber()).replaceAll("[\\,\\[\\]\\ ]", ""), String.valueOf(doc.getNoOffice())}));
                output.append("\n");
            }

            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void writePatients(HashSet<Patient> patients) throws IOException {
        File file = new File(DIR_PATH + "/Patients1.csv");
        try {
            FileWriter output = new FileWriter(file);

            String[] header = { "First Name", "Last Name", "CNP", "Phone Number"};
            output.append(String.join(",", header));
            output.append("\n");

            for(Patient patient : patients) {
                output.append(String.join(",", new String[] {patient.getFirstName(), patient.getLastName(), patient.getCnp(), java.util.Arrays.toString(patient.getPhoneNumber()).replaceAll("[\\,\\[\\]\\ ]", "")}));
                output.append("\n");
            }

            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void writeDocuments(HashSet<Document> documents){
        File file = new File(DIR_PATH + "/Documents1.csv");
        try {
            FileWriter output = new FileWriter(file);

            String[] header = {"Type" ,"Doctor First Name", "Doctor Last Name", "Date", "Others"};
            output.append(String.join(",", header));
            output.append("\n");

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for(Document doc : documents) {

                output.append(String.join(",", new String[] {doc.getClass().getSimpleName(), doc.getDoctor().getFirstName(), doc.getDoctor().getLastName(), df.format(doc.getDate()), doc.getArgs()}));
                output.append("\n");
            }

            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void writeAppointments(TreeSet<Appointment> apps){
        File file = new File(DIR_PATH + "/Appointments1.csv");
        try {
            FileWriter output = new FileWriter(file);

            String[] header = {"Type", "Doctor First Name", "Doctor Last Name", "Patient First Name", "Patient Last Name", "Date", "Others"};
            output.append(String.join(",", header));
            output.append("\n");

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for(Appointment doc : apps) {

                output.append(String.join(",", new String[] {doc.getClass().getSimpleName(), doc.getDoctor().getFirstName(), doc.getDoctor().getLastName(), doc.getPatient().getLastName(), doc.getPatient().getLastName(), df.format(doc.getDate()), doc.getArgs()}));
                output.append("\n");
            }

            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static FileWriterService FileWriterService(){
        if(instance == null)
            instance = new FileWriterService();
        return instance;
    }
}
