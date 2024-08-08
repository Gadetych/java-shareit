package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.practicum.shareit.Marker;

@Data
public class UserDto {
    @Null
    private Long id;
    @NotNull(groups = Marker.Create.class)
    private String name;
    @NotNull(groups = Marker.Create.class)
    @Email(groups = Marker.Create.class)
    private String email;
}
