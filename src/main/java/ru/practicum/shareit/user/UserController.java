package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        User user = userService.create(userDto);
        log.info("Пользователь с id {} создан", user.getId());
        return user;
    }

    @PatchMapping("/{userId}")
    public User update(@Validated(Update.class) @RequestBody UserDto userDto, @PathVariable Long userId) {
        userDto.setId(userId);
        User user = userService.update(userDto);
        log.info("Пользователь с id {} обновлен", user.getId());
        return user;
    }

    @GetMapping
    public List<UserDto> getAll() {
        List<UserDto> users = userService.getAll();
        log.info("Получен список всех пользователей");
        return users;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        UserDto userDto = userService.getById(userId);
        log.info("Получена информация о пользователе с id {}", userDto.getId());
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
        log.info("Удален пользователь с  id {}", userId);
    }
}
