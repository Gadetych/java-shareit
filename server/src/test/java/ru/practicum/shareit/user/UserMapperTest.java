package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    private final User model = new User();
    private final UserDto dto = new UserDto();

    @BeforeEach
    void setUp() {
        model.setId(1L);
        model.setName("QWd1d");
        model.setEmail("QWd1d@mail.ru");

        dto.setId(1L);
        dto.setName("QWd1d");
        dto.setEmail("QWd1d@mail.ru");
    }

    @Test
    void modelToDto() {
        UserDto result = UserMapper.modelToDto(model);

        assertEquals(result, dto, "result: " + result + "\n model: " + model);
    }

    @Test
    void dtoToModel() {
        User result = UserMapper.dtoToModel(dto);

        assertEquals(model, result, "model: " + result + "\n dto: " + dto);
    }
}