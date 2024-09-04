package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("memory")
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public boolean exists(Long itemId) {
        return itemRepository.exists(itemId);
    }

    @Override
    public ItemDto save(Long ownerId, ItemDto dto) {
        userService.exists(ownerId);
        Item item = itemMapper.dtoToModel(dto);
        return itemMapper.modelToDto(itemRepository.save(ownerId, item));
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
        Item item = itemMapper.dtoToModel(dto);
        return itemMapper.modelToDto(itemRepository.update(ownerId, item));
    }

    @Override
    public ItemDto get(Long ownerId, Long id) {
        userService.exists(ownerId);
        if (!exists(id)) {
            throw new NotFoundException("Getting. Item not found with id " + id);
        }
        return itemMapper.modelToDto(itemRepository.get(ownerId, id));
    }

    @Override
    public List<ItemDto> getAll(Long ownerId) {
        userService.exists(ownerId);
        return itemRepository.getAll(ownerId).stream()
                .map(itemMapper::modelToDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(Long ownerId, String text) {
        userService.exists(ownerId);
        return itemRepository.search(ownerId, text).stream()
                .map(itemMapper::modelToDto)
                .toList();
    }
}
