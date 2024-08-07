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
        log.info("==> Updating user {}", user);
        User oldUser = users.get(user.getId());
        String name = user.getName();
        if (name != null) {
            oldUser.setName(name);
        }
        String email = user.getEmail();
        if (email != null) {
            oldUser.setEmail(email);
        }
        log.info("<== Updating user {}", user);
        return oldUser;
    }

    @Override
    public User get(Long id) {
        log.info("==> Getting user id = {}", id);
        User user = users.get(id);
        log.info("<== Getting user {}", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        log.info("==> Getting all users");
        List<User> result = (List<User>) users.values();
        log.info("<== Getting all users");
        return result;
    }

    @Override
    public void delete(Long id) {
        log.info("==> Deleting user with id = {}", id);
        users.remove(id);
        log.info("<== Deleted user with id = {}", id);
    }

    private long createId() {
        return ++maxId;
    }
}
