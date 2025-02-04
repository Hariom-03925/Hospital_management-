package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Doctor {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty specialization;

    public Doctor() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.specialization = new SimpleStringProperty();
    }

    public Doctor(int id, String name, String specialization) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.specialization = new SimpleStringProperty(specialization);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty specializationProperty() {
        return specialization;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSpecialization() {
        return specialization.get();
    }

    public void setSpecialization(String specialization) {
        this.specialization.set(specialization);
    }
}
