package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.BookingWithItemAndUser;

public interface BookingWithItemRepository extends JpaRepository<BookingWithItemAndUser, Long> {
}
