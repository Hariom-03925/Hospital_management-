package ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import service.AppointmentService; // Make sure you have this service
import model.Appointment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentUI {
    private ListView<String> appointmentListView;
    private AppointmentService appointmentService = new AppointmentService();
    private List<Appointment> appointmentList;

    public VBox createAppointmentTabContent() {
        VBox vbox = new VBox(10);
        appointmentListView = new ListView<>();
        refreshAppointmentListView();

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> refreshAppointmentListView());

        Button addButton = new Button("Add Appointment");
        addButton.setOnAction(e -> openAddAppointmentDialog());

        Button updateButton = new Button("Update Appointment");
        updateButton.setOnAction(e -> openUpdateAppointmentDialog());

        Button deleteButton = new Button("Delete Appointment");
        deleteButton.setOnAction(e -> deleteSelectedAppointment());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        vbox.getChildren().addAll(appointmentListView, buttonBox, refreshButton);
        return vbox;
    }

    private void refreshAppointmentListView() {
        appointmentList = appointmentService.getAllAppointments();
        appointmentListView.getItems().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Date format

        for (Appointment appointment : appointmentList) {
            String appointmentInfo = "Appointment ID: " + appointment.getId() +
                                      ", Patient ID: " + appointment.getPatientId() +
                                      ", Doctor ID: " + appointment.getDoctorId() +
                                      ", Date: " + (appointment.getAppointmentDate() != null ? sdf.format(appointment.getAppointmentDate()) : "N/A") +
                                      ", Status: " + appointment.getStatus();
            appointmentListView.getItems().add(appointmentInfo);
        }
    }

    private void openAddAppointmentDialog() {
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setTitle("Add Appointment");

        TextField patientIdField = new TextField();
        TextField doctorIdField = new TextField();
        DatePicker appointmentDatePicker = new DatePicker();
        TextField statusField = new TextField();

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Patient ID:"), patientIdField,
            new Label("Doctor ID:"), doctorIdField,
            new Label("Appointment Date:"), appointmentDatePicker,
            new Label("Status:"), statusField
        ));

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                // Convert LocalDate to java.util.Date
                Date appointmentDate = appointmentDatePicker.getValue() != null ?
                        java.sql.Date.valueOf(appointmentDatePicker.getValue()) : null;
                return new Appointment(0, Integer.parseInt(patientIdField.getText()),
                        Integer.parseInt(doctorIdField.getText()), appointmentDate,
                        statusField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(appointment -> {
            appointmentService.addAppointment(appointment);
            refreshAppointmentListView();
        });
    }

    private void openUpdateAppointmentDialog() {
        String selectedAppointmentInfo = appointmentListView.getSelectionModel().getSelectedItem();
        if (selectedAppointmentInfo != null) {
            int selectedAppointmentId = Integer.parseInt(selectedAppointmentInfo.split(",")[0].split(":")[1].trim());
            Appointment selectedAppointment = appointmentList.stream()
                .filter(appointment -> appointment.getId() == selectedAppointmentId)
                .findFirst()
                .orElse(null);

            if (selectedAppointment != null) {
                Dialog<Appointment> dialog = new Dialog<>();
                dialog.setTitle("Update Appointment");

                TextField patientIdField = new TextField(String.valueOf(selectedAppointment.getPatientId()));
                TextField doctorIdField = new TextField(String.valueOf(selectedAppointment.getDoctorId()));
                DatePicker appointmentDatePicker = new DatePicker();
                TextField statusField = new TextField(selectedAppointment.getStatus());

                // Set the value for the DatePicker
                if (selectedAppointment.getAppointmentDate() != null) {
                    appointmentDatePicker.setValue(selectedAppointment.getAppointmentDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                }

                dialog.getDialogPane().setContent(new VBox(10,
                    new Label("Patient ID:"), patientIdField,
                    new Label("Doctor ID:"), doctorIdField,
                    new Label("Appointment Date:"), appointmentDatePicker,
                    new Label("Status:"), statusField
                ));

                ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButtonType) {
                        // Convert LocalDate to java.util.Date
                        Date appointmentDate = appointmentDatePicker.getValue() != null ?
                                java.sql.Date.valueOf(appointmentDatePicker.getValue()) : null;
                        selectedAppointment.setPatientId(Integer.parseInt(patientIdField.getText()));
                        selectedAppointment.setDoctorId(Integer.parseInt(doctorIdField.getText()));
                        selectedAppointment.setAppointmentDate(appointmentDate);
                        selectedAppointment.setStatus(statusField.getText());
                        return selectedAppointment;
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(appointment -> {
                    if (appointment != null) {
                        appointmentService.updateAppointment(appointment);
                        refreshAppointmentListView();
                    }
                });
            }
        } else {
            showAlert("No Appointment Selected", "Please select an appointment to update.");
        }
    }

    private void deleteSelectedAppointment() {
        String selectedAppointmentInfo = appointmentListView.getSelectionModel().getSelectedItem();
        if (selectedAppointmentInfo != null) {
            int selectedAppointmentId = Integer.parseInt(selectedAppointmentInfo.split(",")[0].split(":")[1].trim());
            Appointment selectedAppointment = appointmentList.stream()
                .filter(appointment -> appointment.getId() == selectedAppointmentId)
                .findFirst()
                .orElse(null);

            if (selectedAppointment != null) {
                appointmentService.deleteAppointment(selectedAppointment.getId());
                refreshAppointmentListView();
            }
        } else {
            showAlert("No Appointment Selected", "Please select an appointment to delete.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

