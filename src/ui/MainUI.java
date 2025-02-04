package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.stage.Stage;

// Importing UI classes
import ui.BillUI;
import ui.AppointmentUI;
import ui.UserUI;
import ui.PatientUI;
import ui.DoctorUI;

public class MainUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hospital  Management  System");

        // Background image setup with a fitting size
        Image img = new Image("file:///C:/Users/hp/Desktop/Assignments/HospitalManagementSystem/image.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage background = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        // Create the root AnchorPane and set the background
        AnchorPane root = new AnchorPane();
        root.setBackground(new Background(background));

        // Header
        HBox header = new HBox();
        header.setStyle("-fx-pref-width: 100%;"); // Full width for the header box
        Label titleLabel = new Label("Hospital Management System");
        titleLabel.getStyleClass().add("header");
         // Allow the label to grow with the window size
        titleLabel.setMaxWidth(Double.MAX_VALUE); // Allow the label to expand fully
        // Center the text horizontally
        header.getChildren().add(titleLabel);
        header.setStyle("-fx-alignment: center;");


        // Navigation Bar
        HBox navbar = new HBox();
        Button homeButton = new Button("Home");
        homeButton.getStyleClass().add("nav-button");
        Button aboutButton = new Button("About");
        aboutButton.getStyleClass().add("nav-button");
        Button contactButton = new Button("Contact");  
        contactButton.getStyleClass().add("nav-button");
        navbar.getChildren().addAll(homeButton, aboutButton, contactButton);
        navbar.getStyleClass().add("navbar");

        TabPane tabPane = new TabPane();
        tabPane.setVisible(false);

        // Bill, Appointment, User, Patient, Doctor UI classes
        BillUI billUI = new BillUI();
        AppointmentUI appointmentUI = new AppointmentUI();
        UserUI userUI = new UserUI();
        PatientUI patientUI = new PatientUI();
        DoctorUI doctorUI = new DoctorUI();

        // Create tabs for each functionality
        Tab billTab = new Tab("Bills", billUI.createBillTabContent());
        Tab appointmentTab = new Tab("Appointments", appointmentUI.createAppointmentTabContent());
        Tab userTab = new Tab("Users", userUI.createUserTabContent());
        Tab patientTab = new Tab("Patients", patientUI.createPatientTabContent());
        Tab doctorTab = new Tab("Doctors", doctorUI.createDoctorTabContent());

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(billTab, appointmentTab, userTab, patientTab, doctorTab);

        // About Section
        VBox aboutSection = new VBox();
        aboutSection.setVisible(false); // Initially hidden
        Label aboutLabel = new Label("About Hospital Management System");
        Text aboutText = new Text("This system helps hospitals manage their operations. \n"
                + "It provides features to manage bills, appointments, users, patients, and doctors. \n"
                + "Developed in 2024, it aims to improve healthcare efficiency.");
        aboutSection.getChildren().addAll(aboutLabel, aboutText);
        aboutSection.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.8);");

        // Contact Section
        VBox contactSection = new VBox();
        contactSection.setVisible(false); // Initially hidden
        Label contactLabel = new Label("Contact Us");
        Text contactText = new Text("For inquiries, reach us at:\n"
                + "Phone: +123 456 7890\n"
                + "Email: support@hospital.com\n"
                + "Address: 123 Healthcare Avenue, varanasi, India");
        contactSection.getChildren().addAll(contactLabel, contactText);
        contactSection.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.8);");

        // Footer
        HBox footer = new HBox();
        Label footerLabel = new Label("© 2024 Healthcare Inc.");
        footer.getStyleClass().add("footer");
        footer.getChildren().add(footerLabel);

        // Main layout
     
        VBox mainLayout = new VBox(header, navbar, tabPane, aboutSection, contactSection, footer);
        mainLayout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        Scene scene = new Scene(mainLayout, 1024, 768); // Increased the scene size for fullscreen layout


        // Show the tabs when "Home" button is clicked
        homeButton.setOnAction(e -> {
            tabPane.setVisible(true);  // Show the TabPane
            aboutSection.setVisible(false); // Hide the "About" section
            contactSection.setVisible(false); // Hide the "Contact" section
        });

        // Show the About section when "About" button is clicked
        aboutButton.setOnAction(e -> {
            tabPane.setVisible(false); // Hide the TabPane
            aboutSection.setVisible(true);  // Show the "About" section
            contactSection.setVisible(false); // Hide the "Contact" section
        });

        // Show the Contact section when "Contact" button is clicked
        contactButton.setOnAction(e -> {
            tabPane.setVisible(false); // Hide the TabPane
            aboutSection.setVisible(false); // Hide the "About" section
            contactSection.setVisible(true);  // Show the "Contact" section
        });

        // Set the CSS file
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
