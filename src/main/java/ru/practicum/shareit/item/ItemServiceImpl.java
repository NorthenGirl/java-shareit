package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public Item create(ItemDto itemDto) {
        Item item = itemMapper.mapToItem(itemDto);
        User owner = userRepository.getById(itemDto.getOwnerId()).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %s не найден",itemDto.getOwnerId()));
        });
        item.setOwner(owner);
        return itemRepository.create(item);
    }

    @Override
    public Item update(ItemDto itemDto) {
        validateUserId(itemDto.getOwnerId());
        Item item = itemMapper.mapToItem(itemDto);
        User owner = userRepository.getById(itemDto.getOwnerId()).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %s не найден",itemDto.getOwnerId()));
        });
        item.setOwner(owner);
        return itemRepository.update(item);
    }

    @Override
    public void delete(Long id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        validateUserId(userId);
        return itemRepository.getAll(userId).stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(Long id) {
        Item item = itemRepository.getById(id).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещь с id %s не найдена",id));
        });
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItems(text)
                .stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void validateUserId(Long userId) {
        if (userRepository.getById(userId) == null) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден", userId));
        }
    }
}
