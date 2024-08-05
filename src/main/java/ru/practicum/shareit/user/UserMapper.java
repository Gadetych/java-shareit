package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.Mapper;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    @Override
    public UserDto ModelToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    @Override
    public User DtoToModel(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
