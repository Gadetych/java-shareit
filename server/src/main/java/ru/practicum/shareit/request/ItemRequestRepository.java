package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select distinct ir from ItemRequest ir join fetch ir.items where ir.requestorId = :requestorId order by ir.created desc")
//    @Query("select ir from ItemRequest as ir where ir.requestorId = :requestorId order by ir.created desc")
    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(@Param("requestorId") long requestorId);

    @Query("select ir from ItemRequest as ir order by ir.created desc")
    List<ItemRequest> findAllOrderByCreatedDesc();
}
