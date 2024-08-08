package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@Qualifier("memory")
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long itemId;

    @Override
    public boolean exists(Long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public boolean existsItemForUser(Long ownerId, Long itemId) {
        return items.get(itemId).getOwnerId().equals(ownerId);
    }

    @Override
    public Item save(Long ownerId, Item item) {
        log.debug("==> Saving item {}", item);
        item.setId(createItemId());
        items.put(item.getId(), item);
        log.debug("<== Saving item {}", item);
        return item;
    }

    @Override
    public Item update(Long ownerId, Item item) {
        log.debug("==> Updating item {}", item);
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
        log.debug("<== Updating item {}", savedItem);
        return savedItem;
    }

    @Override
    public Item get(Long ownerId, Long itemId) {
        log.debug("==> Getting item {}", itemId);
        Item savedItem = items.get(itemId);
        log.debug("<== Getting item {}", savedItem);
        return savedItem;
    }

    @Override
    public List<Item> getAll(Long ownerId) {
        log.debug("==> Getting all items from user {}", ownerId);
        List<Item> itemsList = items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .toList();
        log.debug("<== Getting all items {} from user {}", items, ownerId);
        return itemsList;
    }

    @Override
    public List<Item> search(Long ownerId, String text) {
        log.debug("==> Searching for items with text {}", text);
        if (text.isEmpty()) {
            return List.of();
        }
        List<Item> itemList = List.copyOf(items.values().stream()
                .filter(item -> {
                    boolean a = StringUtils.containsIgnoreCase(item.getName(), text);
                    boolean b = StringUtils.containsIgnoreCase(item.getDescription(), text);
                    return item.getAvailable() && (a || b);
                })
                .toList());
        log.debug("<== Searching for items {} with text {}", itemList, text);
        return itemList;
    }

    private long createItemId() {
        return ++itemId;
    }
}
