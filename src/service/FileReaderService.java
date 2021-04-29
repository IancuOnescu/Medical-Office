package service;

import model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

public class FileReaderService {
    private static FileReaderService instance = null;

    private static final String DIR_PATH = "files";

    private FileReaderService(){}

    private ArrayList<String[]> loadFile(String FILE_PATH) throws IOException {
        ArrayList<String[]> ret = new ArrayList<>();

        BufferedReader csvReader = new BufferedReader(new FileReader(DIR_PATH + FILE_PATH));
        String row = csvReader.readLine();
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            ret.add(data);
        }
        csvReader.close();
        return ret;
    }

    public void loadDoctors(Office office) throws IOException {
        ArrayList<String[]> content = loadFile("/Doctors.csv");
        HashSet<Doctor> docs = office.getDoctors();

        for(String[] data : content) {
            Doctor doc = new Doctor(data[0], data[1], data[2], Arrays.stream(data[3].split("")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new), Integer.parseInt(data[4]));
            docs.add(doc);
        }
    }

    public HashSet<Patient> loadPatients() throws IOException {
        ArrayList<String[]> content = loadFile("/Patients.csv");
        HashSet<Patient> pats = new HashSet<>();

        for(String[] data : content) {
            Patient pat = new Patient(data[0], data[1], data[2], Arrays.stream(data[3].split("")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new));
            pats.add(pat);
        }

        return pats;
    }

    public void loadAppointments(Office office) throws IOException, ParseException {
        ArrayList<String[]> content = loadFile("/Appointments.csv");

        HashSet<Doctor> docs = office.getDoctors();
        HashSet<Patient> pats = loadPatients();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for(String[] data : content) {
            Appointment ap;
            String[] args = data[6].split("/");
            Doctor doctor = null;
            Patient patient = null;

            for(Doctor doc : docs)
                if(doc.getLastName().equals(data[2]) && doc.getFirstName().equals(data[1]))
                    doctor = doc;

            for(Patient pat : pats)
                if(pat.getLastName().equals(data[4]) && pat.getFirstName().equals(data[3]))
                    patient = pat;

            switch (data[0]) {
                case "Examination":
                    ap = new Examination(patient, doctor, df.parse(data[5]), args[0]); break;
                case "Endoscopy":
                    ap = new Endoscopy(patient, doctor, df.parse(data[5]), Integer.parseInt(args[0]), args[1]); break;
                case "Colonoscopy":
                    ap = new Colonoscopy(patient, doctor, df.parse(data[5]), Integer.parseInt(args[0]), args[1]); break;
                case "Auscultation":
                    ap = new Auscultation(patient, doctor, df.parse(data[5]), Integer.parseInt(args[0]), Double.parseDouble(args[1])); break;
                default: return;
            }

            assert doctor != null;
            TreeSet<Appointment> apps = doctor.getAppointments();
            apps.add(ap);
        }
    }

    public void loadDocuments(Office office) throws IOException, ParseException {
        ArrayList<String[]> content = loadFile("/Documents.csv");

        HashSet<Doctor> docs = office.getDoctors();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for(String[] data : content) {
            Document document;
            String[] args = data[4].split("/");
            Doctor doctor = null;

            for(Doctor doc : docs)
                if(doc.getLastName().equals(data[2]) && doc.getFirstName().equals(data[1]))
                    doctor = doc;

            switch (data[0]){
                case "Refferal":
                    document = new Refferal(df.parse(data[3]), doctor, args); break;
                case "Certificate":
                    document = new Certificate(df.parse(data[3]), doctor, args); break;
                case "Prescription":
                    document = new Prescription(df.parse(data[3]), doctor, args); break;
                default: return;
            }

            assert doctor != null;
            HashSet<Document> documents = doctor.getSignedDocuments();
            documents.add(document);
        }
    }

    public static FileReaderService FileReaderService(){
        if(instance == null)
            instance = new FileReaderService();
        return instance;
    }
}
