package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryTest {
    private final EntityManager em;
    private final UserRepository userRepository;

    private final String name1 = "QWeqr";
    private final String email1 = "QWeqr@gmail.com";
    private final String name2 = "Nasdaf";
    private final String email2 = "Nasdaf@gmail.com";

    @Test
    void existsById() {
        boolean exists = userRepository.existsById(1L);

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

    User createUser() {
        User user = new User();
        user.setId(2L);
        user.setName("Acxvz");
        user.setEmail("Acxvz@gmail.com");
        return user;
    }

    @Test
    void updateUserNameAndEmail() {
        User user = createUser();
        userRepository.updateUserNameAndEmail(user.getId(), user.getName(), user.getEmail());
        User result = userRepository.findById(user.getId()).orElse(null);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void updateUserEmail() {
        User user = createUser();
        userRepository.updateUserEmail(user.getId(), user.getEmail());
        User result = userRepository.findById(user.getId()).orElse(null);

        assertNotNull(result);
        assertEquals(name2, result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void updateUserName() {
        User user = createUser();
        userRepository.updateUserName(user.getId(), user.getName());
        User result = userRepository.findById(user.getId()).orElse(null);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(email2, result.getEmail());
    }

    @Test
    void get() {
        User user = userRepository.findById(1L).orElse(null);

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
        assertEquals(result.getFirst().getId(), 1L);
        assertEquals(result.getLast().getId(), 2L);
    }
}
