package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT id FROM items WHERE owner_id = ?1 AND id = ?2)")
    boolean existsItemForUser(Long ownerId, Long itemId);

    List<Item> findAllItemsByOwnerId(Long ownerId);

    @Query(value = "SELECT i FROM Item i WHERE (lower(i.name) LIKE lower(concat('%', :text, '%')) OR lower(i.description) LIKE lower(concat('%', :text, '%')) ) AND i.available = true")
    List<Item> findContaining(@Param("text") String text);
}

