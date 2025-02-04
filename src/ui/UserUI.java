package ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import service.UserService; // Ensure you have this service for handling database operations
import model.User;

import java.util.List;

public class UserUI {
    private ListView<String> userListView;
    private UserService userService = new UserService();
    private List<User> userList;

    public VBox createUserTabContent() {
        VBox vbox = new VBox(10);
        userListView = new ListView<>();
        refreshUserListView();

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> refreshUserListView());

        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> openAddUserDialog());

        Button updateButton = new Button("Update User");
        updateButton.setOnAction(e -> openUpdateUserDialog());

        Button deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> deleteSelectedUser());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        vbox.getChildren().addAll(userListView, buttonBox, refreshButton);
        return vbox;
    }

    private void refreshUserListView() {
        userList = userService.getAllUsers();
        userListView.getItems().clear();

        for (User user : userList) {
            String userInfo = "User ID: " + user.getId() +
                              ", Username: " + user.getUsername() +
                              ", Role: " + user.getRole();
            userListView.getItems().add(userInfo);
        }
    }

    private void openAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add User");

        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        TextField roleField = new TextField();

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Username:"), usernameField,
            new Label("Password:"), passwordField,
            new Label("Role:"), roleField
        ));

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new User(0, usernameField.getText(), passwordField.getText(), roleField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            userService.addUser(user);
            refreshUserListView();
        });
    }

    private void openUpdateUserDialog() {
        String selectedUserInfo = userListView.getSelectionModel().getSelectedItem();
        if (selectedUserInfo != null) {
            int selectedUserId = Integer.parseInt(selectedUserInfo.split(",")[0].split(":")[1].trim());
            User selectedUser = userList.stream()
                .filter(user -> user.getId() == selectedUserId)
                .findFirst()
                .orElse(null);

            if (selectedUser != null) {
                Dialog<User> dialog = new Dialog<>();
                dialog.setTitle("Update User");

                TextField usernameField = new TextField(selectedUser.getUsername());
                TextField passwordField = new TextField(selectedUser.getPassword());
                TextField roleField = new TextField(selectedUser.getRole());

                dialog.getDialogPane().setContent(new VBox(10,
                    new Label("Username:"), usernameField,
                    new Label("Password:"), passwordField,
                    new Label("Role:"), roleField
                ));

                ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButtonType) {
                        selectedUser.setUsername(usernameField.getText());
                        selectedUser.setPassword(passwordField.getText());
                        selectedUser.setRole(roleField.getText());
                        return selectedUser;
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(user -> {
                    if (user != null) {
                        userService.updateUser(user);
                        refreshUserListView();
                    }
                });
            }
        } else {
            showAlert("No User Selected", "Please select a user to update.");
        }
    }

    private void deleteSelectedUser() {
        String selectedUserInfo = userListView.getSelectionModel().getSelectedItem();
        if (selectedUserInfo != null) {
            int selectedUserId = Integer.parseInt(selectedUserInfo.split(",")[0].split(":")[1].trim());
            User selectedUser = userList.stream()
                .filter(user -> user.getId() == selectedUserId)
                .findFirst()
                .orElse(null);

            if (selectedUser != null) {
                userService.deleteUser(selectedUser.getId());
                refreshUserListView();
            }
        } else {
            showAlert("No User Selected", "Please select a user to delete.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
