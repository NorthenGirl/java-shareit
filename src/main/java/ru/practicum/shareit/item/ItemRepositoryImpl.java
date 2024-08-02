package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OtherOwnerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();
    private  final HashMap<Long, List<Long>> itemsByUser = new HashMap<>();
    private  Long id = 0L;

    @Override
    public Item create(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        if (itemsByUser.get(item.getOwner().getId()) == null) {
            List<Long> itemsIds = new ArrayList<>();
            itemsIds.add(item.getId());
            itemsByUser.put(item.getOwner().getId(), itemsIds);
        } else {
            itemsByUser.get(item.getOwner().getId()).add(item.getId());
        }
        return item;
    }

    @Override
    public Item update(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException(String.format("Вещь с id %s не найдена", item.getId()));
        }
        Item updateItem = items.get(item.getId());
        if (!item.getOwner().getId().equals(updateItem.getOwner().getId())) {
            throw new OtherOwnerException(String.format("Пользователь с идентификатором {}" +
                    " не является владельцем обновляемой вещи", item.getOwner().getId()));
        }
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        items.put(item.getId(), updateItem);
        return updateItem;
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
        itemsByUser.remove(items.get(id).getOwner().getId());
    }

    @Override
    public List<Item> getAll(Long userId) {
        return itemsByUser.get(userId).stream()
                .map(items::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        (item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
