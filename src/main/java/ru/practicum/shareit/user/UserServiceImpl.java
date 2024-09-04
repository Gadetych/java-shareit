package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Qualifier("memory")
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void exists(Long id) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
    }

    @Override
    public void exists(String email) {
        if (userRepository.exists(email)) {
            throw new EmailAlreadyExistException("This email = " + email + " already exists");
        }
    }

    @Override
    public UserDto save(UserDto userDto) {
        exists(userDto.getEmail());
        User user = userMapper.dtoToModel(userDto);
        return userMapper.modelToDto(userRepository.save(user));
    }

    @Override
    public UserDto update(UserDto userDto) {
        exists(userDto.getId());
        exists(userDto.getEmail());
        User user = userMapper.dtoToModel(userDto);
        return userMapper.modelToDto(userRepository.update(user));
    }

    @Override
    public UserDto get(Long id) {
        exists(id);
        return userMapper.modelToDto(userRepository.get(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(userMapper::modelToDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }
}
