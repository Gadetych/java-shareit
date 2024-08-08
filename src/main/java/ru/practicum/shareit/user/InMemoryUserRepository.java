package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository("memory")
@Slf4j
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private long maxId;

    @Override
    public boolean exists(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean exists(String email) {
        return emails.contains(email);
    }

    @Override
    public User save(User user) {
        log.debug("==> Saving user {}", user);
        user.setId(createId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.debug("<== Saving user {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("==> Updating user {}", user);
        User oldUser = users.get(user.getId());
        String name = user.getName();
        if (name != null) {
            oldUser.setName(name);
        }
        String email = user.getEmail();
        if (email != null) {
            oldUser.setEmail(email);
            emails.add(email);
        }
        log.debug("<== Updating user {}", user);
        return oldUser;
    }

    @Override
    public User get(Long id) {
        log.debug("==> Getting user id = {}", id);
        User user = users.get(id);
        log.debug("<== Getting user {}", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        log.debug("==> Getting all users");
        List<User> result = (List<User>) users.values();
        log.debug("<== Getting all users");
        return result;
    }

    @Override
    public void delete(Long id) {
        log.debug("==> Deleting user with id = {}", id);
        User user = users.remove(id);
        emails.remove(user.getEmail());
        log.debug("<== Deleted user with id = {}", id);
    }

    private long createId() {
        return ++maxId;
    }
}
