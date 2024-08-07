package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@Qualifier("memory")
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Map<Long, Item>> userItems = new HashMap<>();
    private long itemId;

    @Override
    public boolean exists(Long itemId) {
        return userItems.values().stream()
                .anyMatch(map -> map.containsKey(itemId));
    }

    @Override
    public Item save(Long ownerId, Item item) {
        log.info("==> Saving item {}", item);
        item.setId(createItemId());
        Map<Long, Item> items = userItems.computeIfAbsent(ownerId, k -> new HashMap<>());
        items.put(item.getId(), item);
        log.info("<== Saving item {}", item);
        return item;
    }

    @Override
    public Item update(Long ownerId, Item item) {
        log.info("==> Updating item {}", item);
        Map<Long, Item> items = userItems.get(ownerId);
        if (items == null) {
            throw new NotFoundException("Updating. Item" + item + " not found for user id = " + ownerId);
        }
        Item savedItem = items.get(item.getId());
        if (item.getName() != null) {
            savedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            savedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            savedItem.setAvailable(item.getAvailable());
        }
        log.info("<== Updating item {}", savedItem);
        return savedItem;
    }

    @Override
    public Item get(Long ownerId, Long itemId) {
        log.info("==> Getting item {}", itemId);
        Collection<Map<Long, Item>> items = userItems.values();
        Item savedItem = items.stream()
                .filter(map -> map.containsKey(itemId))
                .map(map -> map.get(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Updating. Item not found with id " + itemId));
        log.info("<== Getting item {}", savedItem);
        return savedItem;
    }

    @Override
    public List<Item> getAll(Long ownerId) {
        log.info("==> Getting all items from user {}", ownerId);
        List<Item> items = List.copyOf(userItems.get(ownerId).values());
        log.info("<== Getting all items {} from user {}", items, ownerId);
        return items;
    }

    @Override
    public List<Item> search(Long ownerId, String text) {
        log.info("==> Searching for items with text {}", text);
        Map<Long, Item> items = new HashMap<>();
        for (Map<Long, Item> map : userItems.values()) {
            items.putAll(map);
        }
        if (text.isEmpty()) {
            return List.of();
        }
        String finalText = text.toLowerCase();
        List<Item> itemList = List.copyOf(items.values().stream()
                .filter(item -> {
                    boolean a = StringUtils.containsIgnoreCase(item.getName(), finalText);
                    boolean b = StringUtils.containsIgnoreCase(item.getDescription(), finalText);
                    return item.getAvailable() && (a || b);
                })
                .toList());
        log.info("<== Searching for items {} with text {}", itemList, finalText);
        return itemList;
    }

    private long createItemId() {
        return ++itemId;
    }
}
