package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    public boolean exists(Long itemId) {
        boolean exists = itemRepository.existsById(itemId);
        if (!exists) {
            throw new NotFoundException("Item not found with id " + itemId);
        }
        return true;
    }

    @Override
    @Transactional
    public ItemDto save(Long ownerId, ItemDto dto) {
        userService.exists(ownerId);
        dto.setOwnerId(ownerId);
        Item item = ItemMapper.dtoToModel(dto);
        return ItemMapper.modelToDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long ownerId, ItemDto dto) {
        if (!itemRepository.existsItemForUser(ownerId, dto.getId())) {
            throw new NotFoundException("Updating. Item id = " + dto.getId() + " not found for user id = " + ownerId);
        }
        userService.exists(ownerId);
        exists(dto.getId());
        Item item = itemRepository.findById(dto.getId()).get();
        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return ItemMapper.modelToDto(itemRepository.save(item));
    }

    public Map<String, BookingDto> getLastAndNextBookings(Long ownerId) {
        Map<String, BookingDto> bookingDtoMap = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        List<Booking> list = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByEndDesc(ownerId, now);
        Booking lastBookingModel = list.stream().findFirst().orElse(null);
        Booking nextBookingModel = null;
        long min = Long.MAX_VALUE;
        for (Booking booking : list) {
            long diff = (booking.getStart().toEpochSecond(ZoneOffset.UTC) - now.toEpochSecond(ZoneOffset.UTC));
            if (diff < min) {
                min = diff;
                nextBookingModel = booking;
            }
        }
        BookingDto nextBooking = null;
        if (nextBookingModel != null) {
            nextBooking = BookingMapper.modelToDtoResponse(nextBookingModel);
        }
        BookingDto lastBooking = null;
        if (lastBookingModel != null) {
            lastBooking = BookingMapper.modelToDtoResponse(lastBookingModel);
        }
        bookingDtoMap.put("lastBooking", lastBooking);
        bookingDtoMap.put("nextBooking", nextBooking);
        return bookingDtoMap;
    }

    @Override
    public ItemDto get(Long ownerId, Long id) {
        userService.exists(ownerId);
        exists(id);
        Item model = itemRepository.findById(id).get();
        Map<String, BookingDto> bookingDtoMap = getLastAndNextBookings(ownerId);
        ItemDto result = ItemMapper.modelToDto(model);
        result.setLastBooking(bookingDtoMap.get("lastBooking"));
        result.setNextBooking(bookingDtoMap.get("nextBooking"));
        return result;
    }

    @Override
    public List<ItemDto> findAllItemsByOwnerId(Long ownerId) {
        userService.exists(ownerId);
        return itemRepository.findAllItemsByOwnerId(ownerId).stream()
                .map(ItemMapper::modelToDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(Long ownerId, String text) {
        userService.exists(ownerId);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<Item> result = itemRepository.findContaining(text);
        return result.stream()
                .map(ItemMapper::modelToDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDtoResponse createComment(CommentDtoCreate dto, long authorId, long itemId) {
        bookingRepository.findByBookerIdAndItemIdOrderByStart(authorId, itemId).stream()
                .filter((b) -> b.getEnd().isBefore(dto.getCreated()))
                .findFirst().orElseThrow(() -> new NotValidException("Creating comment failed"));
        Comment model = CommentMapper.dtoToModel(dto);
        User author = UserMapper.dtoToModel(userService.get(authorId));
        model.setAuthor(author);
        Item item = itemRepository.findById(itemId).orElse(null);
        model.setItem(item);
        return CommentMapper.modelToDto(commentRepository.save(model));
    }

    @Override
    public ItemWithCommentsDtoResponse getItemWithComments(long itemId) {
        exists(itemId);
        Item model = itemRepository.findById(itemId).get();
        List<CommentDtoResponse> comments = commentRepository.findAllByItemIdOrderById(itemId).stream()
                .map(CommentMapper::modelToDto)
                .toList();
        Map<String, BookingDto> bookingDtoMap = getLastAndNextBookings(model.getOwnerId());
        return ItemMapper.modelToDtoWithComments(model, comments, bookingDtoMap);
    }
}
