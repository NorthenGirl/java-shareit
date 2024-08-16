package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.create(UserMapper.mapToUser(userDto));
        return  UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userRepository.update(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void delete(Long id) {
         userRepository.delete(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.getById(id).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %s не найден",id));
        });
        return UserMapper.mapToUserDto(user);
    }
}
