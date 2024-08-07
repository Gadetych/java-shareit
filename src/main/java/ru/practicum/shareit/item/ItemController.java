package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestController;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;
    private final ItemRequestController itemRequestController;

    @PostMapping
    @Validated(Marker.Create.class)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id")
                          @NotNull
                          @Positive Long ownerId,
                          @RequestBody
                          @Valid ItemDto dto) {
        dto.setOwnerId(ownerId);
        return itemService.save(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id")
                          @NotNull
                          @Positive Long ownerId,
                          @PathVariable
                          @NotNull
                          @Positive Long itemId,
                          @RequestBody
                          @Valid ItemDto dto) {
        dto.setId(itemId);
        dto.setOwnerId(ownerId);
        return itemService.update(ownerId, dto);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader("X-Sharer-User-Id")
                       @NotNull
                       @Positive Long ownerId,
                       @PathVariable
                       @NotNull
                       @Positive Long itemId) {
        return itemService.get(ownerId, itemId);
    }

    @GetMapping
    List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id")
                         @NotNull
                         @Positive Long ownerId) {
        return itemService.getAll(ownerId);
    }

    @GetMapping("/search")
    List<ItemDto> search(@RequestHeader("X-Sharer-User-Id")
                         @NotNull
                         @Positive Long ownerId,
                         @RequestParam("text") String text) {
        return itemService.search(ownerId, text);
    }
}
