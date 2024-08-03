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

    @Override
    public ItemDto create(ItemDto itemDto) {
        Item item = ItemMapper.mapToItem(itemDto);
        User owner = userRepository.getById(itemDto.getOwnerId()).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %s не найден",itemDto.getOwnerId()));
        });
        item.setOwner(owner);
        return ItemMapper.mapToItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(ItemDto itemDto) {
        validateUserId(itemDto.getOwnerId());
        Item item = ItemMapper.mapToItem(itemDto);
        User owner = userRepository.getById(itemDto.getOwnerId()).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %s не найден",itemDto.getOwnerId()));
        });
        item.setOwner(owner);
        return ItemMapper.mapToItemDto(itemRepository.update(item));
    }

    @Override
    public void delete(Long id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        validateUserId(userId);
        return itemRepository.getAll(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(Long id) {
        Item item = itemRepository.getById(id).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещь с id %s не найдена",id));
        });
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItems(text)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void validateUserId(Long userId) {
        if (userRepository.getById(userId) == null) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден", userId));
        }
    }
}
