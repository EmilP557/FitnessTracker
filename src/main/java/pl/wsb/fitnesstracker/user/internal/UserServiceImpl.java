package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(final Long id, final User user) {
        log.info("Updating User with id {}", id);
        return userRepository.findById(id)
                .map(existing -> {
                    userRepository.delete(existing);
                    userRepository.flush();
                    User updated = new User(
                            user.getFirstName(),
                            user.getLastName(),
                            user.getBirthdate(),
                            user.getEmail()
                    );
                    return userRepository.save(updated);
                })
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    @Override
    public void deleteUser(final Long id) {
        log.info("Deleting User with id {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}