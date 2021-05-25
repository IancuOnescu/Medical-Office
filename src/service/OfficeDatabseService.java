package service;

import model.*;
import config.DatabaseConnection;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class OfficeDatabseService {
    private final Office office;
    private LoggingService logService = new LoggingService();

    public OfficeDatabseService(Office office) throws SQLException {
        this.office = office;
        initDatabase();
    }

    public void createDoctorTable () throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS medical_office.doctor (
                 id int PRIMARY KEY,
                	lastName VARCHAR(10) NOT NULL,
                	firstName VARCHAR(10) NOT NULL,
                	cnp VARCHAR(20) NOT NULL,
                	phoneNumber VARCHAR(12),
                 noOffice integer
                );""";
        try {
            Statement stmt = DatabaseConnection.getInstance().createStatement();
            stmt.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void createPatientTable () throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS medical_office.patient (
                 id int PRIMARY KEY,
                	lastName VARCHAR(10) NOT NULL,
                	firstName VARCHAR(10) NOT NULL,
                	cnp VARCHAR(20) NOT NULL,
                	phoneNumber VARCHAR(12),
                	doctor_id int NOT NULL,
                 FOREIGN KEY (doctor_id) REFERENCES medical_office.doctor(id));""";

        try {
            Statement stmt = DatabaseConnection.getInstance().createStatement();
            stmt.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void createAppointmentTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS medical_office.appointment (
                 id int PRIMARY KEY,
                	patient_id int NOT NULL,
                	doctor_id int NOT NULL,
                	date DATE NOT NULL,
                	cost int,
                	type VARCHAR(20),
                 arguments VARCHAR(50),
                 FOREIGN KEY (doctor_id) REFERENCES medical_office.doctor(id),
                 FOREIGN KEY (patient_id) REFERENCES medical_office.patient(id));""";
        try {
            Statement stmt = DatabaseConnection.getInstance().createStatement();
            stmt.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void createCertificateTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS medical_office.certificate (
                 id int PRIMARY KEY,
                	patient_id int NOT NULL,
                	doctor_id int NOT NULL,
                	date DATE NOT NULL,
                 description VARCHAR(50),
                 FOREIGN KEY (doctor_id) REFERENCES medical_office.doctor(id),
                 FOREIGN KEY (patient_id) REFERENCES medical_office.patient(id));""";
        try {
            Statement stmt = DatabaseConnection.getInstance().createStatement();
            stmt.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void createPrescriptionTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS medical_office.prescription (
                 id int PRIMARY KEY,
                	patient_id int NOT NULL,
                	doctor_id int NOT NULL,
                	date DATE NOT NULL,
                 meds VARCHAR(50),
                 FOREIGN KEY (doctor_id) REFERENCES medical_office.doctor(id),
                 FOREIGN KEY (patient_id) REFERENCES medical_office.patient(id));""";
        try {
            Statement stmt = DatabaseConnection.getInstance().createStatement();
            stmt.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void createRefferalTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS medical_office.refferal (
                 id int PRIMARY KEY,
                	to_doctor_id int NOT NULL,
                	doctor_id int NOT NULL,
                	date DATE NOT NULL,
                 office VARCHAR(50),
                 FOREIGN KEY (doctor_id) REFERENCES medical_office.doctor(id),
                 FOREIGN KEY (to_doctor_id) REFERENCES medical_office.doctor(id));""";
        try {
            Statement stmt = DatabaseConnection.getInstance().createStatement();
            stmt.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void loadDoctors() {
        HashSet<Doctor> docs = office.getDoctors();

        String sql = "select * from medical_office.doctor";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                String lastName = result.getString("lastName");
                String firstName = result.getString("firstName");
                String phoneNumber = result.getString("phoneNumber");
                String cnp = result.getString("cnp");
                int noOffice = result.getInt("noOffice");
                docs.add(new Doctor(firstName, lastName, cnp, Arrays.stream(phoneNumber.split("")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new), noOffice));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public HashSet<Patient> loadPatients() {
        HashSet<Patient> pats = new HashSet<>();

        String sql = "select * from medical_office.patient";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                String lastName = result.getString("lastName");
                String firstName = result.getString("firstName");
                String phoneNumber = result.getString("phoneNumber");
                String cnp = result.getString("cnp");
                pats.add(new Patient(firstName, lastName, cnp, Arrays.stream(phoneNumber.split("")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new)));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return pats;
    }

    public void loadAppointments() {
        HashSet<Doctor> docs = office.getDoctors();
        HashSet<Patient> pats = loadPatients();

        String sql = "select * from medical_office.appointment";

        try(PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                int patient_id = result.getInt("patient_id");
                int doctor_id = result.getInt("doctor_id");
                Date date = result.getTimestamp("date");
                int cost = result.getInt("cost");
                String type = result.getString("type");
                String args = result.getString("arguments");

                Appointment ap;
                Doctor doctor = null;
                Patient patient = null;

                for(Doctor doc : docs)
                    if(doc.hashCode() == doctor_id)
                        doctor = doc;

                for(Patient pat : pats)
                    if(pat.hashCode() == patient_id)
                        patient = pat;

                switch (type) {
                    case "examination":
                        ap = new Examination(patient, doctor, date, args); break;
                    case "endoscopy":
                        ap = new Endoscopy(patient, doctor, date, cost, args); break;
                    case "colonoscopy":
                        ap = new Colonoscopy(patient, doctor, date, cost, args); break;
                    case "auscultation":
                        ap = new Auscultation(patient, doctor, date, cost, Double.parseDouble(args)); break;
                    default: return;
                }

                assert doctor != null;
                TreeSet<Appointment> apps = doctor.getAppointments();
                apps.add(ap);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDocuments() {
        HashSet<Doctor> docs = office.getDoctors();
        HashSet<Patient> pats = loadPatients();

        String[] documents = new String[]{"certificate", "refferal", "prescription"};
        String[] column = new String[]{"description", "office", "meds"};

        String sql = "select * from medical_office.";

        for(int i = 0; i < 3; i++) {
            String sql_aux = sql + documents[i];
            try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql_aux)) {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    int doctor_id = result.getInt("doctor_id");
                    Date date = result.getTimestamp("date");
                    String[] args = result.getString(column[i]).split("");

                    Document document;
                    Doctor doctor = null;

                    for (Doctor doc : docs)
                        if (doc.hashCode() == doctor_id)
                            doctor = doc;

                    switch (documents[i]) {
                        case "refferal":
                            document = new Refferal(date, doctor, args);
                            break;
                        case "certificate":
                            document = new Certificate(date, doctor, args);
                            break;
                        case "prescription":
                            document = new Prescription(date, doctor, args);
                            break;
                        default:
                            return;
                    }

                    assert doctor != null;
                    HashSet<Document> d = doctor.getSignedDocuments();
                    d.add(document);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDatabase() throws SQLException {
        createDoctorTable();
        createPatientTable();
        createAppointmentTable();
        createCertificateTable();
        createPrescriptionTable();
        createRefferalTable();
        loadDoctors();
        loadAppointments();
        loadDocuments();
    }
}
