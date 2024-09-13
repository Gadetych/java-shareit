package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserMapper;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public CommentDtoResponse modelToDto(Comment model) {
        CommentDtoResponse dto = new CommentDtoResponse();
        dto.setId(model.getId());
        dto.setText(model.getText());
        dto.setAuthor(userMapper.modelToDto(model.getAuthor()));
        dto.setItem(itemMapper.modelToDto(model.getItem()));
        dto.setCreated(model.getCreated());
        dto.setAuthorName(dto.getAuthor().getName());
        return dto;
    }

    public Comment dtoToModel(CommentDtoCreate dto) {
        Comment model = new Comment();
        model.setText(dto.getText());
        model.setCreated(dto.getCreated());
        return model;
    }
}
