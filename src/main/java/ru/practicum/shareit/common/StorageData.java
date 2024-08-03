package ru.practicum.shareit.common;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class StorageData {
    @NotNull(groups = {Update.class})
    Long id;
}
