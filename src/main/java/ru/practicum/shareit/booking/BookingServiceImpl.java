package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.AccessStatusItem;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BookingAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public void exists(long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Booking with id " + bookingId + " not found");
        }
    }

    @Override
    public void exists(long bookingId, long ownerId) {
        if (!bookingRepository.existsByOwnerId(bookingId, ownerId)) {
            throw new BookingAccessException("User with id " + ownerId + " not owner of booking with id " + bookingId);
        }
    }

    @Override
    @Transactional
    public BookingDto save(BookingDtoCreate dtoCreate) {
        ItemDto itemDto = itemService.get(dtoCreate.getBookerId(), dtoCreate.getItemId());
        if (!itemDto.getAvailable()) {
            throw new UnavailableItemException("Saving. Item is not available, id = " + itemDto.getId());
        }
        UserDto bookerDto = userService.get(dtoCreate.getBookerId());
        Booking booking = BookingMapper.dtoToModel(dtoCreate, itemDto, bookerDto);
        return BookingMapper.modelToDtoResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateStatusBooking(long bookingId, long ownerId, boolean approved) {
        exists(bookingId, ownerId);
        userService.exists(ownerId);
        AccessStatusItem newStatus;
        if (approved) {
            newStatus = AccessStatusItem.APPROVED;
        } else {
            newStatus = AccessStatusItem.REJECT;
        }
        bookingRepository.updateBooking(bookingId, ownerId, newStatus);
        return BookingMapper.modelToDtoResponse(bookingRepository.findById(bookingId).get());
    }

    @Override
    public BookingDto get(long bookingId, long userId) {
        exists(bookingId);
        Booking result = bookingRepository.findByBookingId(bookingId, userId).orElseThrow(() -> new BookingAccessException("Denied access for user id = " + userId));
        return BookingMapper.modelToDtoResponse(result);
    }

    @Override
    public List<BookingDto> getAllByBooker(long bookerId, BookingState state) {
        userService.exists(bookerId);
        List<Booking> modelList;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case PAST -> modelList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(bookerId, now);
            case FUTURE -> modelList = bookingRepository.findAllByBookerIdAndStartAfterOrderByEndDesc(bookerId, now);
            case WAITING ->
                    modelList = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(bookerId, AccessStatusItem.WAITING);
            case REJECTED ->
                    modelList = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(bookerId, AccessStatusItem.REJECT);
            case CURRENT -> modelList = bookingRepository.findAllByBookerIdAndNowBetweenOrderByEndDesc(bookerId, now);
            default -> modelList = bookingRepository.findAllByBookerIdOrderByEndDesc(bookerId);
        }
        return modelList.stream()
                .map(BookingMapper::modelToDtoResponse)
                .toList();
    }

    @Override
    public List<BookingDto> getAllByOwner(long ownerId, BookingState state) {
        userService.exists(ownerId);
        List<Booking> modelList;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case PAST -> modelList = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(ownerId, now);
            case FUTURE -> modelList = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByEndDesc(ownerId, now);
            case WAITING ->
                    modelList = bookingRepository.findAllByItemOwnerIdAndStatusOrderByEndDesc(ownerId, AccessStatusItem.WAITING);
            case REJECTED ->
                    modelList = bookingRepository.findAllByItemOwnerIdAndStatusOrderByEndDesc(ownerId, AccessStatusItem.REJECT);
            case CURRENT -> modelList = bookingRepository.findAllByItemOwnerIdAndNowBetweenOrderByEndDesc(ownerId, now);
            default -> modelList = bookingRepository.findAllByItemOwnerIdOrderByEndDesc(ownerId);
        }
        return modelList.stream()
                .map(BookingMapper::modelToDtoResponse)
                .toList();
    }

}
