package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDtoCreate {
    @Positive
    private long itemId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private long bookerId;
    private AccessStatusItem status = AccessStatusItem.WAITING;

    @AssertTrue
    public boolean isValidDate() {
        return start.isBefore(end);
    }
}
