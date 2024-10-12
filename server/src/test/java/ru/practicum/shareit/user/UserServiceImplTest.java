package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final User model = new User(null, "QWd1d", "QWd1d@mail.ru");
    private final UserDto dto = new UserDto(null, "QWd1d", "QWd1d@mail.ru");
    @Mock
    private UserRepository userRepositoryMock;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldByIdNotExists() {
        long id = 2L;
        when(userRepositoryMock.existsById(id))
                .thenReturn(false);

        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.exists(id));

        assertEquals("User not found with id: " + id, exception.getMessage());
    }

    @Test
    void shouldByEmailNotExists() {
        String email = "test@test.com";
        when(userRepositoryMock.existsByUserEmail(email))
                .thenReturn(true);

        final EmailAlreadyExistException exception = Assertions.assertThrows(EmailAlreadyExistException.class, () -> userService.exists(email));

        assertEquals("This email = " + email + " already exists", exception.getMessage());
    }

    @Test
    void save() {
        when(userRepositoryMock.save(model))
                .thenAnswer(invocation -> {
                    model.setId(1L);
                    return model;
                });

        UserDto result = userService.save(dto);

        assertEquals(1L, result.getId());
    }

    @Test
    void notSaveBecauseEmailAlreadyExists() {
        when(userRepositoryMock.existsByUserEmail(dto.getEmail()))
                .thenReturn(true);

        EmailAlreadyExistException exception = Assertions.assertThrows(EmailAlreadyExistException.class, () -> userService.save(dto));
        assertEquals("This email = " + dto.getEmail() + " already exists", exception.getMessage());
    }

    private void setMocks() {
        dto.setId(2L);
        model.setId(2L);
        when(userRepositoryMock.existsById(dto.getId()))
                .thenReturn(true);
        when(userRepositoryMock.existsByUserEmail(dto.getEmail()))
                .thenReturn(false);
        when(userRepositoryMock.findById(dto.getId()))
                .thenReturn(Optional.of(model));
    }

    @Test
    void updateUserNameAndEmail() {
        setMocks();
        userService.update(dto);

        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .updateUserNameAndEmail(anyLong(), anyString(), anyString());
    }

    @Test
    void updateUserName() {
        dto.setEmail(null);
        setMocks();
        userService.update(dto);

        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .updateUserName(anyLong(), anyString());
    }

    @Test
    void updateUserEmail() {
        dto.setName(null);
        setMocks();
        userService.update(dto);

        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .updateUserEmail(anyLong(), anyString());
    }

    @Test
    void notUpdateUser() {
        dto.setEmail(null);
        dto.setName(null);
        setMocks();
        userService.update(dto);

        Mockito.verify(userRepositoryMock, Mockito.times(0))
                .updateUserEmail(anyLong(), anyString());

        Mockito.verify(userRepositoryMock, Mockito.times(0))
                .updateUserName(anyLong(), anyString());

        Mockito.verify(userRepositoryMock, Mockito.times(0))
                .updateUserNameAndEmail(anyLong(), anyString(), anyString());
    }

    @Test
    void getALl() {
        when(userRepositoryMock.findAll(any(Sort.class))).thenReturn(new ArrayList<>());
        List<UserDto> result = userService.getAll();

        assertNotNull(result);
    }

    @Test
    void deleteUser() {
        userService.delete(dto.getId());

        Mockito.verify(userRepositoryMock, Mockito.times(1)).deleteById(dto.getId());
    }
}