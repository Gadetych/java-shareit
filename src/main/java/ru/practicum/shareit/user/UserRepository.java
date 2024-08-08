package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    boolean exists(Long id);

    boolean exists(String email);

    User save(User user);

    User update(User user);

    User get(Long id);

    List<User> getAll();

    void delete(Long id);
}
