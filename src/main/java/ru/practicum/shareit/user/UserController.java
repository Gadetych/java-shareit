package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    @Validated(Marker.Create.class)
    public UserDto save(@RequestBody
                        @Valid UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{id}")
    UserDto update(@NotNull
                   @Positive
                   @PathVariable Long id,
                   @RequestBody
                   @Valid UserDto userDto) {
        userDto.setId(id);
        return userService.update(userDto);
    }

    @GetMapping("/{id}")
    UserDto get(@PathVariable
                @NotNull
                @Positive Long id) {
        return userService.get(id);
    }

    @GetMapping
    List<UserDto> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable
                @NotNull
                @Positive Long id) {
        userService.delete(id);
    }
}
