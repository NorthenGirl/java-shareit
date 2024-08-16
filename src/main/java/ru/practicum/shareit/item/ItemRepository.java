package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Item update(Item item);

    void delete(Long id);

    List<Item> getAll(Long userId);

    Optional<Item> getById(Long id);

    List<Item> searchItems(String text);
}
