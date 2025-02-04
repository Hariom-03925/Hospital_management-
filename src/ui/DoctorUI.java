package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Doctor;
import service.DoctorService;

import java.util.List;

public class DoctorUI {
    private TableView<Doctor> doctorTableView;
    private DoctorService doctorService = new DoctorService();
    private List<Doctor> doctorList;

    public VBox createDoctorTabContent() {
        VBox vbox = new VBox(10);

        // Initialize TableView
        doctorTableView = new TableView<>();
        initializeTableColumns();

        refreshDoctorTableView();

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> refreshDoctorTableView());

        Button addButton = new Button("Add Doctor");
        addButton.setOnAction(e -> openAddDoctorDialog());

        Button updateButton = new Button("Update Doctor");
        updateButton.setOnAction(e -> openUpdateDoctorDialog());

        Button deleteButton = new Button("Delete Doctor");
        deleteButton.setOnAction(e -> deleteSelectedDoctor());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        vbox.getChildren().addAll(doctorTableView, buttonBox, refreshButton);
        return vbox;
    }

    private void initializeTableColumns() {
        TableColumn<Doctor, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<Doctor, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Doctor, String> specializationColumn = new TableColumn<>("Specialization");
        specializationColumn.setCellValueFactory(cellData -> cellData.getValue().specializationProperty());

        doctorTableView.getColumns().addAll(idColumn, nameColumn, specializationColumn);
    }

    private void refreshDoctorTableView() {
        doctorList = doctorService.getAllDoctors();
        ObservableList<Doctor> observableDoctors = FXCollections.observableArrayList(doctorList);
        doctorTableView.setItems(observableDoctors);
    }

    private void openAddDoctorDialog() {
        Dialog<Doctor> dialog = new Dialog<>();
        dialog.setTitle("Add Doctor");

        TextField nameField = new TextField();
        TextField specializationField = new TextField();

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Name:"), nameField,
            new Label("Specialization:"), specializationField
        ));

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Doctor(0, nameField.getText(), specializationField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(doctor -> {
            doctorService.addDoctor(doctor);
            refreshDoctorTableView();
        });
    }

    private void openUpdateDoctorDialog() {
        Doctor selectedDoctor = doctorTableView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            Dialog<Doctor> dialog = new Dialog<>();
            dialog.setTitle("Update Doctor");

            TextField nameField = new TextField(selectedDoctor.getName());
            TextField specializationField = new TextField(selectedDoctor.getSpecialization());

            dialog.getDialogPane().setContent(new VBox(10,
                new Label("Name:"), nameField,
                new Label("Specialization:"), specializationField
            ));

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    selectedDoctor.setName(nameField.getText());
                    selectedDoctor.setSpecialization(specializationField.getText());
                    return selectedDoctor;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(doctor -> {
                doctorService.updateDoctor(doctor);
                refreshDoctorTableView();
            });
        } else {
            showAlert("No Doctor Selected", "Please select a doctor to update.");
        }
    }

    private void deleteSelectedDoctor() {
        Doctor selectedDoctor = doctorTableView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            doctorService.deleteDoctor(selectedDoctor.getId());
            refreshDoctorTableView();
        } else {
            showAlert("No Doctor Selected", "Please select a doctor to delete.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
