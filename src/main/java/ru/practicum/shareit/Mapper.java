package ru.practicum.shareit;

public interface Mapper <M, D> {
    D ModelToDto(M model);

    M DtoToModel(D dto);
}
