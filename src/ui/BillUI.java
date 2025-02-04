package ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import service.BillService;
import model.Bill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BillUI {
    private ListView<String> billListView;
    private BillService billService = new BillService();
    private List<Bill> billList;

    public VBox createBillTabContent() {
        VBox vbox = new VBox(10);
        billListView = new ListView<>();
        refreshBillListView();

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> refreshBillListView());

        Button addButton = new Button("Add Bill");
        addButton.setOnAction(e -> openAddBillDialog());

        Button updateButton = new Button("Update Bill");
        updateButton.setOnAction(e -> openUpdateBillDialog());

        Button deleteButton = new Button("Delete Bill");
        deleteButton.setOnAction(e -> deleteSelectedBill());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        vbox.getChildren().addAll(billListView, buttonBox, refreshButton);
        return vbox;
    }

    private void refreshBillListView() {
        billList = billService.getAllBills();
        billListView.getItems().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Date format

        for (Bill bill : billList) {
            String billInfo = "Bill ID: " + bill.getId() +
                              ", Amount: " + bill.getAmount() +
                              ", Status: " + bill.getPaymentStatus() +
                              ", Payment Date: " + (bill.getPaymentDate() != null ? sdf.format(bill.getPaymentDate()) : "N/A");
            billListView.getItems().add(billInfo);
        }
    }

    private void openAddBillDialog() {
        Dialog<Bill> dialog = new Dialog<>();
        dialog.setTitle("Add Bill");

        TextField patientIdField = new TextField();
        TextField amountField = new TextField();
        TextField paymentStatusField = new TextField();
        DatePicker paymentDatePicker = new DatePicker();

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Patient ID:"), patientIdField,
            new Label("Amount:"), amountField,
            new Label("Payment Status:"), paymentStatusField,
            new Label("Payment Date:"), paymentDatePicker
        ));

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                // Convert LocalDate to java.util.Date
                Date paymentDate = paymentDatePicker.getValue() != null ? 
                                   java.sql.Date.valueOf(paymentDatePicker.getValue()) : null;
                return new Bill(0, Integer.parseInt(patientIdField.getText()), 
                                Double.parseDouble(amountField.getText()), 
                                paymentStatusField.getText(), 
                                paymentDate);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(bill -> {
            billService.addBill(bill);
            refreshBillListView();
        });
    }

    private void openUpdateBillDialog() {
        String selectedBillInfo = billListView.getSelectionModel().getSelectedItem();
        if (selectedBillInfo != null) {
            int selectedBillId = Integer.parseInt(selectedBillInfo.split(",")[0].split(":")[1].trim());
            Bill selectedBill = billList.stream()
                .filter(bill -> bill.getId() == selectedBillId)
                .findFirst()
                .orElse(null);

            if (selectedBill != null) {
                Dialog<Bill> dialog = new Dialog<>();
                dialog.setTitle("Update Bill");

                TextField patientIdField = new TextField(String.valueOf(selectedBill.getPatientId()));
                TextField amountField = new TextField(String.valueOf(selectedBill.getAmount()));
                TextField paymentStatusField = new TextField(selectedBill.getPaymentStatus());
                DatePicker paymentDatePicker = new DatePicker();

                // Set the value for the DatePicker
                if (selectedBill.getPaymentDate() != null) {
                    paymentDatePicker.setValue(selectedBill.getPaymentDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                }

                dialog.getDialogPane().setContent(new VBox(10,
                    new Label("Patient ID:"), patientIdField,
                    new Label("Amount:"), amountField,
                    new Label("Payment Status:"), paymentStatusField,
                    new Label("Payment Date:"), paymentDatePicker
                ));

                ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButtonType) {
                        // Convert LocalDate to java.util.Date
                        Date paymentDate = paymentDatePicker.getValue() != null ? 
                                           java.sql.Date.valueOf(paymentDatePicker.getValue()) : null;
                        selectedBill.setPatientId(Integer.parseInt(patientIdField.getText()));
                        selectedBill.setAmount(Double.parseDouble(amountField.getText()));
                        selectedBill.setPaymentStatus(paymentStatusField.getText());
                        selectedBill.setPaymentDate(paymentDate);
                        return selectedBill;
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(bill -> {
                    if (bill != null) {
                        billService.updateBill(bill);
                        refreshBillListView();
                    }
                });
            }
        } else {
            showAlert("No Bill Selected", "Please select a bill to update.");
        }
    }

    private void deleteSelectedBill() {
        String selectedBillInfo = billListView.getSelectionModel().getSelectedItem();
        if (selectedBillInfo != null) {
            int selectedBillId = Integer.parseInt(selectedBillInfo.split(",")[0].split(":")[1].trim());
            Bill selectedBill = billList.stream()
                .filter(bill -> bill.getId() == selectedBillId)
                .findFirst()
                .orElse(null);

            if (selectedBill != null) {
                billService.deleteBill(selectedBill.getId());
                refreshBillListView();
            }
        } else {
            showAlert("No Bill Selected", "Please select a bill to delete.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
