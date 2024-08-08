package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    void exists(Long id);

    void exists(String email);

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto get(Long id);

    List<UserDto> getAll();

    void delete(Long id);
}
