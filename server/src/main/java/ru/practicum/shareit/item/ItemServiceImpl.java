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
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDtoCreate;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

    private Map<String, BookingDto> getLastAndNextBookings(List<Booking> list, LocalDateTime now) {
        Map<String, BookingDto> bookingDtoMap = new HashMap<>();
        Booking lastBookingModel = null;
        LocalDateTime last = LocalDateTime.MIN;
        for (Booking booking : list) {
            if (booking.getEnd().isAfter(last)) {
                last = booking.getEnd();
                lastBookingModel = booking;
            }
        }

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
        LocalDateTime now = LocalDateTime.now();
        List<Booking> list = bookingRepository.findAllByItemIdAndEndBeforeOrderByEndDesc(model.getId(), now);
        Map<String, BookingDto> bookingDtoMap = getLastAndNextBookings(list, now);
        ItemDto result = ItemMapper.modelToDto(model);
        result.setLastBooking(bookingDtoMap.get("lastBooking"));
        result.setNextBooking(bookingDtoMap.get("nextBooking"));
        return result;
    }

    @Override
    public List<ItemWithCommentsDtoResponse> findAllItemsByOwnerId(Long ownerId) {
        userService.exists(ownerId);
        LocalDateTime now = LocalDateTime.now();
        List<Item> items = itemRepository.findAllItemsByOwnerId(ownerId);
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(ownerId, now);
        Map<Item, List<Booking>> bookingsByItemId = new HashMap<>();
        for (Booking booking : bookings) {
            Item item = booking.getItem();
            if (!bookingsByItemId.containsKey(item)) {
                List<Booking> newBookings = new ArrayList<>();
                newBookings.add(booking);
                bookingsByItemId.put(item, newBookings);
            }
            bookingsByItemId.get(item).add(booking);
        }
        Map<Item, Map<String, BookingDto>> itemsByLastAndNextBookingDto = new HashMap<>();
        for (Map.Entry<Item, List<Booking>> entry : bookingsByItemId.entrySet()) {
            Item item = entry.getKey();
            Map<String, BookingDto> lastAndNextBooking = getLastAndNextBookings(entry.getValue(), now);
            itemsByLastAndNextBookingDto.put(item, lastAndNextBooking);
        }

        List<Comment> comments = commentRepository.findAllByItemOwnerId(ownerId);
        Map<Item, List<CommentDtoResponse>> commentsByItemId = new HashMap<>();
        for (Comment comment : comments) {
            Item item = comment.getItem();
            CommentDtoResponse commentDtoResponse = CommentMapper.modelToDto(comment);
            List<CommentDtoResponse> list = commentsByItemId.get(item);
            if (list == null) {
                list = new ArrayList<>();
                list.add(commentDtoResponse);
                commentsByItemId.put(item, list);
            }
            list.add(commentDtoResponse);
        }
        List<ItemWithCommentsDtoResponse> result = new ArrayList<>();
        for (Item item : items) {
            ItemWithCommentsDtoResponse itemWithCommentsDtoResponse = ItemMapper.modelToDtoWithComments(item, commentsByItemId.get(item), itemsByLastAndNextBookingDto.get(item));
            result.add(itemWithCommentsDtoResponse);
        }
        return result;
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
    public ItemWithCommentsDtoResponse getItemWithComments(long itemId, long userId) {
        LocalDateTime now = LocalDateTime.now();
        exists(itemId);
        Item model = itemRepository.findById(itemId).get();
        List<CommentDtoResponse> comments = commentRepository.findAllByItemIdOrderById(itemId).stream()
                .map(CommentMapper::modelToDto)
                .toList();
        Map<String, BookingDto> bookingDtoMap = null;
        if (model.getOwnerId().equals(userId)) {
            List<Booking> list = bookingRepository.findAllByItemIdAndEndBeforeOrderByEndDesc(itemId, now);
            bookingDtoMap = getLastAndNextBookings(list, now);
        }
        return ItemMapper.modelToDtoWithComments(model, comments, bookingDtoMap);
    }
}
