package tn.esprit.microservice3.Services.Interfaces;

import tn.esprit.microservice3.Entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    List<User> getActiveUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    List<User> getUsersByRole(String role);
}
