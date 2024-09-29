package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto creat(@RequestBody BookingDtoCreate dtoCreate,
                            @RequestHeader(value = requestHeader) long bookerId) {
        dtoCreate.setBookerId(bookerId);
        log.info("Creating new booking: {}", dtoCreate);
        return bookingService.create(dtoCreate);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatusBooking(@PathVariable long bookingId,
                                          @RequestHeader(value = requestHeader) long userId,
                                          @RequestParam boolean approved) {
        log.info("Updating status booking: {}, userId: {}, approved: {}", bookingId, userId, approved);
        return bookingService.updateStatusBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable long bookingId,
                               @RequestHeader(value = requestHeader) long userId) {
        log.info("Getting booking: {}, userId: {}", bookingId, userId);
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findAllByBooker(@RequestHeader(value = requestHeader) long bookerId,
                                            @RequestParam(defaultValue = "ALL") BookingState stateParam) {
        log.info("Getting bookings by bookerId: {}, state: {}", bookerId, stateParam);
        return bookingService.findAllByBooker(bookerId, stateParam);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllByOwner(@RequestHeader(value = requestHeader) long ownerId,
                                           @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Getting bookings by ownerId: {}, state: {}", ownerId, state);
        return bookingService.findAllByOwner(ownerId, state);
    }
}
