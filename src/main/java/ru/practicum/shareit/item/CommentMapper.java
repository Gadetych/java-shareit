package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserMapper;

@UtilityClass
public class CommentMapper {

    public static CommentDtoResponse modelToDto(Comment model) {
        CommentDtoResponse dto = new CommentDtoResponse();
        dto.setId(model.getId());
        dto.setText(model.getText());
        dto.setAuthor(UserMapper.modelToDto(model.getAuthor()));
        dto.setItem(ItemMapper.modelToDto(model.getItem()));
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
