package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("memory")
@Slf4j
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long maxId;

    @Override
    public boolean exists(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean exists(String email) {
        return users.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }

    @Override
    public User save(User user) {
        log.info("==> Saving user {}", user);
        user.setId(createId());
        users.put(user.getId(), user);
        log.info("<== Saving user {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        User oldUser = users.get(user.getId());
        String name = user.getName();
        if (name != null) {
            oldUser.setName(name);
        }
        String email = user.getEmail();
        if (email != null) {
            oldUser.setEmail(email);
        }
        return oldUser;
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return (List<User>) users.values();
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    private long createId() {
        return ++maxId;
    }
}
