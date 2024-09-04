package ru.practicum.shareit;

public interface Mapper<M, D> {
    D modelToDto(M model);

    M dtoToModel(D dto);
}
