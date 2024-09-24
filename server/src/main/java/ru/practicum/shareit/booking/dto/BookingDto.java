package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private AccessStatusItem status;

    @AssertTrue
    public boolean isValidDate() {
        return start.isBefore(end);
    }
}
