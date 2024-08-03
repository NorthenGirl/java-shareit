package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.IncorrectObjectStructureException;
import ru.practicum.shareit.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User create(User user) {
        validateEmailUnique(user);
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        validateIdIsSpecified(user);
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден", user.getId()));
        }
        User updateUser = users.get(user.getId());
        if (user.getEmail() != null && !users.get(user.getId()).getEmail().equals(user.getEmail())) {
            validateEmailUnique(user);
        }
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
          updateUser.setEmail(user.getEmail());
        }
        users.put(user.getId(), updateUser);
        return updateUser;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    protected void validateIdIsSpecified(User user) {
        if (user.getId() == null) {
            throw new IncorrectObjectStructureException("id пользователя не указан.");
        }
    }

    protected void validateEmailUnique(User user) {
        for (User us : users.values()) {
            if (user.getEmail().equals(us.getEmail())) {
                throw new DuplicateException(String.format("Пользователь   email: %s уже существует.", user.getEmail()));
            }
        }
    }
}
