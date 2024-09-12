package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.AccessStatusItem;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying
    @Transactional
    @Query(value = "update Booking b set b.status = :status where b.id = :bookingId and b.item.ownerId = :ownerId")
    void updateBooking(@Param("bookingId") long bookingId, @Param("ownerId") long ownerId, @Param("status") AccessStatusItem status);

    @Query(value = "select exists(select b.id from Booking b where b.id = :bookingId and b.item.ownerId = :ownerId)")
    boolean existsByOwnerId(long bookingId, long ownerId);

    @Query(value = "select b from Booking b where b.id = :bookingId and (b.booker.id = :userId or b.item.ownerId = :userId)")
    Optional<Booking> findByBookingId(@Param("bookingId") long bookingId, @Param("userId") long userId);

    List<Booking> findAllByBookerIdOrderByEndDesc(long bookerId);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByEndDesc(long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartAfterOrderByEndDesc(long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatusOrderByEndDesc(long bookerId, AccessStatusItem status);

    @Query(value = "select b from Booking b where b.id = :bookerId and (:now between b.start and b.end)")
    List<Booking> findAllByBookerIdAndNowBetweenOrderByEndDesc(@Param("bookerId") long bookerId, @Param("now") LocalDateTime now);

    List<Booking> findAllByItemOwnerIdOrderByEndDesc(long ownerId);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(long ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByEndDesc(long ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByEndDesc(long ownerId, AccessStatusItem accessStatusItem);

    @Query(value = "select b from Booking b where b.item.id = :ownerId and (:now between b.start and b.end)")
    List<Booking> findAllByItemOwnerIdAndNowBetweenOrderByEndDesc(@Param("ownerId") long ownerId, @Param("now") LocalDateTime now);
}
