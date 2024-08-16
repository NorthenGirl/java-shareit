package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto);

    ItemDto update(ItemDto itemDto);

    void delete(Long id);

    List<ItemDto> getAll(Long userId);

    ItemDto getById(Long id);

    List<ItemDto> searchItems(String text);
}
