package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.shareit.Marker;

@Data
public class UserDto {
    @Null(groups = Marker.Create.class)
    @NotNull(groups = Marker.Update.class)
    @NotNull(groups = Marker.Delete.class)
    @Positive(groups = Marker.Update.class)
    @Positive(groups = Marker.Delete.class)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
