package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

public interface BookingService {

    void exists(long bookingId);

    void exists(long bookingId, long userId);

    BookingDto save(BookingDtoCreate dtoCreate);

    BookingDto updateStatusBooking(long bookingId, long userId, boolean approved);
}
