package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    boolean exists(Long itemId);

    boolean existsItemForUser(Long ownerId, Long itemId);

    Item save(Long ownerId, Item item);

    Item update(Long ownerId, Item item);

    Item get(Long ownerId, Long itemId);

    List<Item> getAll(Long ownerId);

    List<Item> search(Long ownerId, String text);
}
