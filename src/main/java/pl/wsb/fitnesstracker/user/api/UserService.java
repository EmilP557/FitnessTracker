package pl.wsb.fitnesstracker.user.api;

public interface UserService {

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}