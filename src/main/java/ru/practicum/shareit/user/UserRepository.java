package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    User update(User user);

    void delete(Long id);

    List<User> getAll();

    Optional<User> getById(Long id);
}
