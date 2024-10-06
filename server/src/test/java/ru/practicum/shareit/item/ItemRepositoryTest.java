package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRepositoryTest {
    private final ItemRepository itemRepository;
    private final EntityManager em;
    String userName1 = "user1";
    String userName2 = "user2";
    String email1 = "user1@test.com";
    String email2 = "user2@test.com";
    User user1 = new User();
    User user2 = new User();
    String itemName1 = "item1";
    String itemName2 = "item2";
    String itemDesc1 = "desc1";
    String itemDesc2 = "desc2";
    Item item1 = new Item();
    Item item2 = new Item();

    @BeforeEach
    void setUp() {
        user1.setName(userName1);
        user1.setEmail(email1);
        em.persist(user1);

        item1.setName(itemName1);
        item1.setDescription(itemDesc1);
        item1.setOwnerId(user1.getId());
        item1.setAvailable(true);
        em.persist(item1);

        user2.setName(userName2);
        user2.setEmail(email2);
        em.persist(user2);

        item2.setName(itemName2);
        item2.setDescription(itemDesc2);
        item2.setOwnerId(user2.getId());
        item2.setAvailable(true);
        em.persist(item2);
    }

    @Test
    void existsItemForUser() {
        Long userId1 = user1.getId();
        Long itemId1 = item1.getId();

        boolean result = itemRepository.existsItemForUser(userId1, itemId1);

        assertTrue(result);
    }

    @Test
    void findAllItemsByOwnerId() {
        Long userId1 = user1.getId();
        Long itemId1 = item1.getId();

        List<Item> result = itemRepository.findAllItemsByOwnerId(userId1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemId1, result.getFirst().getId());
    }

    @Test
    void findContaining() {
        String text1 = "item";
        String text2 = "desc";
        List<Item> result1 = itemRepository.findContaining(text1);
        List<Item> result2 = itemRepository.findContaining(text2);

        assertEquals(2, result1.size());
        assertEquals(2, result2.size());
    }
}