package service;

import dao.AppointmentDAO;
import model.Appointment;
import java.util.List;

public class AppointmentService {
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    public void addAppointment(Appointment appointment) {
        appointmentDAO.addAppointment(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    public void updateAppointment(Appointment appointment) {
        appointmentDAO.updateAppointment(appointment);
    }

    public void deleteAppointment(int id) {
        appointmentDAO.deleteAppointment(id);
    }
}

