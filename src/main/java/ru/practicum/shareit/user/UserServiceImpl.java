package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void exists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
    }

    @Override
    public void exists(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new EmailAlreadyExistException("This email = " + email + " already exists");
        }
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        exists(userDto.getEmail());
        User user = UserMapper.dtoToModel(userDto);
        return UserMapper.modelToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        exists(userDto.getId());
        exists(userDto.getEmail());
        if (userDto.getName() != null) {
            if (userDto.getEmail() != null) {
                userRepository.updateUserNameAndEmail(userDto.getId(), userDto.getName(), userDto.getEmail());
            } else {
                userRepository.updateUserName(userDto.getId(), userDto.getName());
            }
        } else if (userDto.getEmail() != null) {
            userRepository.updateUserEmail(userDto.getId(), userDto.getEmail());
        }
        return UserMapper.modelToDto(userRepository.findById(userDto.getId()).get());
    }

    @Override
    public UserDto get(Long id) {
        exists(id);
        return UserMapper.modelToDto(userRepository.findById(id).get());
    }

    @Override
    public List<UserDto> getAll() {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        return userRepository.findAll(sortById).stream()
                .map(UserMapper::modelToDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
