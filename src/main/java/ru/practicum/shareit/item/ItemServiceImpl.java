package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
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
        return itemMapper.modelToDto(itemRepository.findById(id).get());
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
}
