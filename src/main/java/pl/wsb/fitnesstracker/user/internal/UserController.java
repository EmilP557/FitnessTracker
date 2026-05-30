package pl.wsb.fitnesstracker.user.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) throws InterruptedException {
        return userMapper.toUserDto(
                userService.createUser(userMapper.toUser(userDto))
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() throws InterruptedException {
        return this.userProvider.findAllUsers().stream()
                .map(this.userMapper::toUserDto)
                .toList();
    }

    @GetMapping("/simple")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getSimpleUsers() throws InterruptedException {
        return this.userProvider.findAllUsers().stream()
                .map(this.userMapper::toUserDto)
                .toList();
    }
}