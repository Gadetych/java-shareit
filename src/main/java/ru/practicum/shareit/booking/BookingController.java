package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class BookingController {
    private final BookingService bookingService;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto creat(@Valid
                            @RequestBody
                            BookingDtoCreate dtoCreate,
                            @RequestHeader(value = requestHeader)
                            @Positive
                            long bookerId) {
        dtoCreate.setBookerId(bookerId);
        return bookingService.save(dtoCreate);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatusBooking(@PathVariable
                                          @Positive
                                          long bookingId,
                                          @RequestHeader(value = requestHeader)
                                          @Positive
                                          long userId,
                                          @RequestParam
                                          boolean approved) {
        return bookingService.updateStatusBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable
                                 @Positive
                                 long bookingId,
                                 @RequestHeader(value = requestHeader)
                                 @Positive
                                 long userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestHeader(value = requestHeader)
                                           @Positive
                                           long bookerId,
                                           @RequestParam(defaultValue = "ALL")
                                           BookingState state) {
        return bookingService.getAllByBooker(bookerId, state);
    }
}
