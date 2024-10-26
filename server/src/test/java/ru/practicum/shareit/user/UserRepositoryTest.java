package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryTest {
    private final EntityManager em;
    private final UserRepository userRepository;

    private User user1 = new User();
    private User user2 = new User();
    private long id1;
    private long id2;
    private final String name1 = "QWeqr";
    private final String email1 = "QWeqr@gmail.com";
    private final String name2 = "Nasdaf";
    private final String email2 = "Nasdaf@gmail.com";

    @BeforeEach
    void setUp() {
        user1.setName(name1);
        user1.setEmail(email1);
        em.persist(user1);
        id1 = user1.getId();

        user2.setName(name2);
        user2.setEmail(email2);
        em.persist(user2);
        id2 = user2.getId();
    }

    @Test
    void existsById() {
        boolean exists = userRepository.existsById(id1);

        assertTrue(exists);
    }

    @Test
    void existsByEmailItsTrue() {
        boolean exists = userRepository.existsByUserEmail(email2);

        assertTrue(exists);
    }

    @Test
    void existsByEmailItsFalse() {
        boolean exists = userRepository.existsByUserEmail("Aqfqfvbqbww@gmail.com");

        assertFalse(exists);
    }

    @Test
    void save() {
        User user = new User();
        user.setName("Acxvz");
        user.setEmail("Acxvz@gmail.com");

        user = userRepository.save(user);

        assertNotNull(user);
        assertEquals("Acxvz", user.getName());
        assertEquals("Acxvz@gmail.com", user.getEmail());
    }


    @Test
    void updateUserNameAndEmail() {
        String newName = "New_Name";
        String newEmail = "New_Email@gmail.com";
        userRepository.updateUserNameAndEmail(id1, newName, newEmail);
        em.clear();
        User result = userRepository.findById(id1).orElse(null);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(newEmail, result.getEmail());
    }

    @Test
    void updateUserEmail() {
        String newEmail = "New_Email@gmail.com";
        userRepository.updateUserEmail(id1, newEmail);
        em.clear();
        User result = userRepository.findById(id1).orElse(null);

        assertNotNull(result);
        assertEquals(name1, result.getName());
        assertEquals(newEmail, result.getEmail());
    }

    @Test
    void updateUserName() {
        String newName = "New_Name";
        userRepository.updateUserName(id1, newName);
        em.clear();
        User result = userRepository.findById(id1).orElse(null);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(email1, result.getEmail());
    }

    @Test
    void get() {
        User user = userRepository.findById(id1).orElse(null);

        assertNotNull(user);
        assertEquals(name1, user.getName());
        assertEquals(email1, user.getEmail());
    }

    @Test
    void getAll() {
        List<User> result = userRepository.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.size(), 2);
        assertEquals(result.getFirst().getId(), id1);
        assertEquals(result.getLast().getId(), id2);
    }
}
