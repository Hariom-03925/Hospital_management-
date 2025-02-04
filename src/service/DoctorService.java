package service;

import dao.DoctorDAO;
import model.Doctor;
import java.util.List;

public class DoctorService {
    private DoctorDAO doctorDAO = new DoctorDAO();

    // Method to add a new doctor
    public void addDoctor(Doctor doctor) {
        doctorDAO.addDoctor(doctor);
    }

    // Method to retrieve all doctors
    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAllDoctors();
    }

    // Method to update a doctor's information
    public void updateDoctor(Doctor doctor) {
        doctorDAO.updateDoctor(doctor);
    }

    // Method to delete a doctor by ID
    public void deleteDoctor(int id) {
        doctorDAO.deleteDoctor(id);
    }

   
}


