package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ItemRequest {
   private Long id;
   private String description;
   private User requestor;
   private LocalDateTime created;
}
