package dao;

import model.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

public class BillDAO {

    // Method to add a bill to the database
    public void addBill(Bill bill) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO bills (patient_id, amount, payment_status, payment_date) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bill.getPatientId());
            statement.setDouble(2, bill.getAmount());
            statement.setString(3, bill.getPaymentStatus());
            statement.setDate(4, new java.sql.Date(bill.getPaymentDate().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all bills from the database
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM bills";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Bill bill = new Bill();
                bill.setId(resultSet.getInt("id"));
                bill.setPatientId(resultSet.getInt("patient_id"));
                bill.setAmount(resultSet.getDouble("amount"));
                bill.setPaymentStatus(resultSet.getString("payment_status"));
                bill.setPaymentDate(resultSet.getDate("payment_date"));
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    // Method to update a bill in the database
    public void updateBill(Bill bill) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE bills SET patient_id = ?, amount = ?, payment_status = ?, payment_date = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bill.getPatientId());
            statement.setDouble(2, bill.getAmount());
            statement.setString(3, bill.getPaymentStatus());
            statement.setDate(4, new java.sql.Date(bill.getPaymentDate().getTime()));
            statement.setInt(5, bill.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a bill from the database
    public void deleteBill(int id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM bills WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
