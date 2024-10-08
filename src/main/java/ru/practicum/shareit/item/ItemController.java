package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto create(@RequestHeader(X_SHARER_USER_ID) Long userId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        itemDto.setOwnerId(userId);
        ItemDto item = itemService.create(itemDto);
        log.info("Вещь с id {} создана", item.getId());
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(X_SHARER_USER_ID) Long userId,
                       @Validated({Update.class}) @RequestBody ItemDto itemDto,
                       @PathVariable Long itemId) {
        itemDto.setId(itemId);
        itemDto.setOwnerId(userId);
        ItemDto item = itemService.update(itemDto);
        log.info("Вещь с id {} обновлена", item.getId());
        return  item;
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        List<ItemDto> itemDtoList = itemService.getAll(userId);
        log.info("Получен список вещей пользователя с id {}", userId);
        return itemDtoList;
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        ItemDto itemDto = itemService.getById(itemId);
        log.info("Получена информация о вещи с id {}", itemId);
        return  itemDto;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        List<ItemDto> itemDtoList = itemService.searchItems(text);
        log.info("Получен список вещей по поиску: {}", text);
        return itemDtoList;
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId) {
        itemService.delete(itemId);
    }
}
