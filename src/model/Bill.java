package model;

import java.util.Date;

public class Bill {
    private int id;
    private int patientId;
    private double amount;
    private String paymentStatus;
    private Date paymentDate;

    // Default constructor
    public Bill() {}

    // Parameterized constructor
    public Bill(int id, int patientId, double amount, String paymentStatus, Date paymentDate) {
        this.id = id;
        this.patientId = patientId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
