package pl.wsb.fitnesstracker.user.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
class UserController {

    private final UserService userService;
    private final UserProvider userProvider;
    private final UserMapper userMapper;

    @Autowired
    UserController(UserService userService, UserProvider userProvider, UserMapper userMapper) {
        this.userService = userService;
        this.userProvider = userProvider;
        this.userMapper = userMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        return userProvider.findAllUsers().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @GetMapping("/simple")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getSimpleUsers() {
        return userProvider.findAllUsers().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        return userProvider.getUser(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByEmail(@RequestParam String email) {
        return userProvider.getUserByEmail(email)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/older/{date}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsersOlderThan(@PathVariable LocalDate date) {
        return userProvider.findAllUsers().stream()
                .filter(u -> u.getBirthdate().isBefore(date))
                .map(userMapper::toUserDto)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userMapper.toUserDto(
                userService.createUser(userMapper.toUser(userDto))
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userMapper.toUserDto(
                userService.updateUser(id, userMapper.toUser(userDto))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}