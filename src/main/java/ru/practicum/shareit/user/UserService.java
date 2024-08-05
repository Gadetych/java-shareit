package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    boolean exists(Long id);

    boolean exists(String email);

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto get(Long id);

    List<UserDto> getAll();

    void delete(Long id);
}
