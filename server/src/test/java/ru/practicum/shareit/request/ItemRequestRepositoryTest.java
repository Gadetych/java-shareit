package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Transactional
@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestRepositoryTest {
    private final ItemRequestRepository itemRequestRepository;
    private final EntityManager em;
    private final String name1 = "user1";
    private final String email1 = "user1@gmail.com";
    private final String name2 = "user2";
    private final String email2 = "user2@gmail.com";
    private ItemRequest request1 = new ItemRequest();
    private ItemRequest request2 = new ItemRequest();
    private ItemRequest request3 = new ItemRequest();
    private String description1 = "description1";
    private String description2 = "description2";
    private String description3 = "description2";
    private long requestorId1;
    private long requestorId2;
    private long requestorId3;
    private User user1 = new User();
    private User user2 = new User();

    @BeforeEach
    void setUp() {
        user1.setName(name1);
        user1.setEmail(email1);
        em.persist(user1);

        user2.setName(name2);
        user2.setEmail(email2);
        em.persist(user2);

        requestorId1 = user1.getId();
        requestorId2 = user1.getId();
        requestorId3 = user2.getId();

        request1.setDescription(description1);
        request1.setRequestorId(requestorId1);
        request1.setCreated(LocalDateTime.now());
        em.persist(request1);

        request2.setDescription(description2);
        request2.setRequestorId(requestorId2);
        request2.setCreated(LocalDateTime.now());
        em.persist(request2);

        request3.setDescription(description3);
        request3.setRequestorId(requestorId3);
        request3.setCreated(LocalDateTime.now());
        em.persist(request3);
    }

    @Test
    void findAllByRequestorIdOrderByCreatedDesc() {
        List<ItemRequest> result = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId1);

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(requestorId1, result.getFirst().getRequestorId());
        assertEquals(requestorId1, result.getLast().getRequestorId());
        assertEquals(result.getFirst().getCreated().isAfter(result.getLast().getCreated()), Boolean.TRUE);
    }

    @Test
    void findAllOrderByCreatedDesc() {
        List<ItemRequest> result = itemRequestRepository.findAllOrderByCreatedDesc();

        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        assertEquals(result.getFirst().getCreated().isAfter(result.getLast().getCreated()), Boolean.TRUE);
    }
}