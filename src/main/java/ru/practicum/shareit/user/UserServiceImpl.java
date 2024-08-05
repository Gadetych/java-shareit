package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Qualifier("memory")
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public boolean exists(Long id) {
        return userRepository.exists(id);
    }

    @Override
    public boolean exists(String email) {
        return userRepository.exists(email);
    }

    @Override
    public UserDto save(UserDto userDto) {
        if (exists(userDto.getEmail())) {
            throw new EmailAlreadyExistException("This email = " + userDto.getEmail() + " already exists");
        }
        User user = userMapper.DtoToModel(userDto);
        return userMapper.ModelToDto(userRepository.save(user));
    }

    @Override
    public UserDto update(UserDto userDto) {
        if (!exists(userDto.getId())) {
            throw new NotFoundException("User not found with id: " + userDto.getId());
        }
        if (exists(userDto.getEmail())) {
            throw new EmailAlreadyExistException("This email = " + userDto.getEmail() + " already exists");
        }
        User user = userMapper.DtoToModel(userDto);
        return userMapper.ModelToDto(userRepository.update(user));
    }

    @Override
    public UserDto get(Long id) {
        if (!exists(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        return userMapper.ModelToDto(userRepository.get(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(userMapper::ModelToDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }
}
