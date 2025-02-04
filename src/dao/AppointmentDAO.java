package dao;

import model.Appointment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

public class AppointmentDAO {
    public void addAppointment(Appointment appointment) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, appointment.getPatientId());
            statement.setInt(2, appointment.getDoctorId());
            statement.setDate(3, new java.sql.Date(appointment.getAppointmentDate().getTime()));
            statement.setString(4, appointment.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM appointments";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getInt("id"));
                appointment.setPatientId(resultSet.getInt("patient_id"));
                appointment.setDoctorId(resultSet.getInt("doctor_id"));
                appointment.setAppointmentDate(resultSet.getDate("appointment_date"));
                appointment.setStatus(resultSet.getString("status"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public void updateAppointment(Appointment appointment) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ?, status = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, appointment.getPatientId());
            statement.setInt(2, appointment.getDoctorId());
            statement.setDate(3, new java.sql.Date(appointment.getAppointmentDate().getTime()));
            statement.setString(4, appointment.getStatus());
            statement.setInt(5, appointment.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(int id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM appointments WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
