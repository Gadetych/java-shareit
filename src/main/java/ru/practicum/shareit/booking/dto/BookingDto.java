package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    @Null(groups = {Marker.Create.class})
    private Long id;
    @NotNull(groups = {Marker.Create.class})
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private Long bookerId;
    private AccessStatusItem status;
}
