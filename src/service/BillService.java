package service;

import dao.BillDAO;
import model.Bill;
import java.util.List;

public class BillService {
    private BillDAO billDAO = new BillDAO();

    public void addBill(Bill bill) {
        billDAO.addBill(bill);
    }

    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }

    public void updateBill(Bill bill) {
        billDAO.updateBill(bill);
    }

    public void deleteBill(int id) {
        billDAO.deleteBill(id);
    }
}

