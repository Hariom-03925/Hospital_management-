package service;

import dao.UserDAO;
import model.User;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public void addUser(User user) {
        userDAO.addUser(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    public void deleteUser(int id) {
        userDAO.deleteUser(id);
    }
}

