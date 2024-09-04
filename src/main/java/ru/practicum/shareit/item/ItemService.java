package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    boolean exists(Long itemId);

    ItemDto save(Long ownerId, ItemDto dto);

    ItemDto update(Long ownerId, ItemDto dto);

    ItemDto get(Long ownerId, Long id);

    List<ItemDto> getAll(Long ownerId);

    List<ItemDto> search(Long ownerId, String text);
}
