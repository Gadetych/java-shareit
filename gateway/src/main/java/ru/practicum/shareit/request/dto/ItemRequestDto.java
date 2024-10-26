package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.practicum.shareit.user.dto.Marker;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    @Null(groups = Marker.Create.class)
    private Long id;
    private long requestorId;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
}
