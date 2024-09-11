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
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    @Validated(Marker.Create.class)
    public ItemDto create(@RequestHeader(requestHeader)
                          @NotNull
                          @Positive Long ownerId,
                          @RequestBody
                          @Valid ItemDto dto) {
        dto.setOwnerId(ownerId);
        return itemService.save(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(requestHeader)
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
    public ItemDto get(@RequestHeader(requestHeader)
                       @NotNull
                       @Positive Long ownerId,
                       @PathVariable
                       @NotNull
                       @Positive Long itemId) {
        return itemService.get(ownerId, itemId);
    }

    @GetMapping
    List<ItemDto> getAll(@RequestHeader(requestHeader)
                         @NotNull
                         @Positive Long ownerId) {
        return itemService.findAllItemsByOwnerId(ownerId);
    }

    @GetMapping("/search")
    List<ItemDto> search(@RequestHeader(requestHeader)
                         @NotNull
                         @Positive Long ownerId,
                         @RequestParam("text") String text) {
        return itemService.search(ownerId, text);
    }
}
