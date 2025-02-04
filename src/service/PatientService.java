
package service;

import dao.PatientDAO;
import model.Patient;
import java.util.List;

public class PatientService {
    private PatientDAO patientDAO = new PatientDAO();

    // Method to add a new patient
    public void addPatient(Patient patient) {
        patientDAO.addPatient(patient);
    }

    // Method to get all patients
    public List<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }

    // Method to delete a patient by ID
    public void deletePatient(int id) {
        patientDAO.deletePatient(id);
    }

    // Method to update patient details (optional)
    public void updatePatient(Patient patient) {
        patientDAO.updatePatient(patient);
    }
}
