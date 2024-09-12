package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.AccessStatusItem;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying
    @Transactional
    @Query(value = "update Booking b set b.status = :status where b.id = :bookingId and b.item.ownerId = :ownerId")
    void updateBooking(@Param("bookingId") long bookingId, @Param("ownerId") long ownerId, @Param("status") AccessStatusItem status);

    @Query(value = "select exists(select b.id from Booking b where b.id = :bookingId and b.item.id = :ownerId)")
    boolean existsByOwnerId(long bookingId, long ownerId);
}
