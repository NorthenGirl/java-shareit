package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User create(UserDto userDto) {
        return  userRepository.create(userMapper.mapToUser(userDto));
    }

    @Override
    public User update(UserDto userDto) {
        return userRepository.update(userMapper.mapToUser(userDto));
    }

    @Override
    public void delete(Long id) {
         userRepository.delete(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.getById(id).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %s не найден",id));
        });
        return userMapper.mapToUserDto(user);
    }
}
