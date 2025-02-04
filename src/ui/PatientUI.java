package ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import service.PatientService;
import model.Patient;

import java.util.List;

public class PatientUI {
    private ListView<String> patientListView;
    private PatientService patientService = new PatientService();
    private List<Patient> patientList;

    public VBox createPatientTabContent() {
        VBox vbox = new VBox(10);
        patientListView = new ListView<>();
        refreshPatientListView();

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> refreshPatientListView());

        Button addButton = new Button("Add Patient");
        addButton.setOnAction(e -> openAddPatientDialog());

        Button updateButton = new Button("Update Patient");
        updateButton.setOnAction(e -> openUpdatePatientDialog());

        Button deleteButton = new Button("Delete Patient");
        deleteButton.setOnAction(e -> deleteSelectedPatient());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        vbox.getChildren().addAll(patientListView, buttonBox, refreshButton);
        return vbox;
    }

    private void refreshPatientListView() {
        patientList = patientService.getAllPatients();
        patientListView.getItems().clear();
        for (Patient patient : patientList) {
            patientListView.getItems().add(patient.getName());
        }
    }

    private void openAddPatientDialog() {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Add Patient");

        TextField nameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneNumberField = new TextField();
        TextField diseaseField = new TextField();

        dialog.getDialogPane().setContent(new VBox(10, 
            new Label("Name:"), nameField,
            new Label("Address:"), addressField,
            new Label("Phone Number:"), phoneNumberField,
            new Label("Disease:"), diseaseField
        ));

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Patient(0, nameField.getText(), addressField.getText(), 
                                   phoneNumberField.getText(), diseaseField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(patient -> {
            patientService.addPatient(patient);
            refreshPatientListView();
        });
    }

    private void openUpdatePatientDialog() {
        String selectedPatientName = patientListView.getSelectionModel().getSelectedItem();
        if (selectedPatientName != null) {
            Patient selectedPatient = patientList.stream()
                .filter(patient -> patient.getName().equals(selectedPatientName))
                .findFirst()
                .orElse(null);

            if (selectedPatient != null) {
                Dialog<Patient> dialog = new Dialog<>();
                dialog.setTitle("Update Patient");

                TextField nameField = new TextField(selectedPatient.getName());
                TextField addressField = new TextField(selectedPatient.getAddress());
                TextField phoneNumberField = new TextField(selectedPatient.getPhoneNumber());
                TextField diseaseField = new TextField(selectedPatient.getDisease());

                dialog.getDialogPane().setContent(new VBox(10, 
                    new Label("Name:"), nameField,
                    new Label("Address:"), addressField,
                    new Label("Phone Number:"), phoneNumberField,
                    new Label("Disease:"), diseaseField
                ));

                ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButtonType) {
                        selectedPatient.setName(nameField.getText());
                        selectedPatient.setAddress(addressField.getText());
                        selectedPatient.setPhoneNumber(phoneNumberField.getText());
                        selectedPatient.setDisease(diseaseField.getText());
                        return selectedPatient;
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(patient -> {
                    patientService.updatePatient(patient);
                    refreshPatientListView();
                });
            }
        } else {
            showAlert("No Patient Selected", "Please select a patient to update.");
        }
    }

    private void deleteSelectedPatient() {
        String selectedPatientName = patientListView.getSelectionModel().getSelectedItem();
        if (selectedPatientName != null) {
            Patient selectedPatient = patientList.stream()
                .filter(patient -> patient.getName().equals(selectedPatientName))
                .findFirst()
                .orElse(null);

            if (selectedPatient != null) {
                patientService.deletePatient(selectedPatient.getId());
                refreshPatientListView();
            }
        } else {
            showAlert("No Patient Selected", "Please select a patient to delete.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
