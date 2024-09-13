package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    //    изначально хотел сделать зависимоть BookingService, но тогда возникает проблема циклических зависимостей
//    решил проблему через зависимости репозитория и маппера, но мне думается, что это не совсем корректное решение.
//    подскажи как сделать лучше
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Override
    public boolean exists(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public ItemDto save(Long ownerId, ItemDto dto) {
        userService.exists(ownerId);
        dto.setOwnerId(ownerId);
        Item item = itemMapper.dtoToModel(dto);
        return itemMapper.modelToDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(Long ownerId, ItemDto dto) {
        if (!itemRepository.existsItemForUser(ownerId, dto.getId())) {
            throw new NotFoundException("Updating. Item id = " + dto.getId() + " not found for user id = " + ownerId);
        }
        userService.exists(ownerId);
        if (!exists(dto.getId())) {
            throw new NotFoundException("Updating. Item not found with id " + dto.getId());
        }
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
        return itemMapper.modelToDto(itemRepository.save(item));
    }

    @Override
    public ItemDto get(Long ownerId, Long id) {
        userService.exists(ownerId);
        if (!exists(id)) {
            throw new NotFoundException("Getting. Item not found with id " + id);
        }
        Item model = itemRepository.findById(id).get();
        LocalDateTime now = LocalDateTime.now();
        Booking lastBookingModel = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByEndDesc(ownerId, now).stream().findFirst().orElse(null);
        List<Booking> list = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByEndDesc(ownerId, now);
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
            nextBooking = bookingMapper.modelToDtoResponse(nextBookingModel);
        }
        BookingDto lastBooking = null;
        if (lastBookingModel != null) {
            lastBooking = bookingMapper.modelToDtoResponse(lastBookingModel);
        }
        ItemDto result = itemMapper.modelToDto(model);
        result.setLastBooking(lastBooking);
        result.setNextBooking(nextBooking);
        return result;
    }

    @Override
    public List<ItemDto> findAllItemsByOwnerId(Long ownerId) {
        userService.exists(ownerId);
        return itemRepository.findAllItemsByOwnerId(ownerId).stream()
                .map(itemMapper::modelToDto)
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
                .map(itemMapper::modelToDto)
                .toList();
    }

    @Override
    public CommentDtoResponse createComment(CommentDtoCreate dto, long authorId, long itemId) {
        Booking booking = bookingRepository.findByBookerIdAndItemIdOrderByStart(authorId, itemId).stream()
                .filter((b) -> b.getStart().isBefore(dto.getCreated()))
                .findFirst().orElseThrow(() -> new NotValidException("Creating comment failed"));
        Comment model = commentMapper.dtoToModel(dto);
        User author = userMapper.dtoToModel(userService.get(authorId));
        model.setAuthor(author);
        Item item = itemRepository.findById(itemId).orElse(null);
        model.setItem(item);
        return commentMapper.modelToDto(commentRepository.save(model));
    }
}
