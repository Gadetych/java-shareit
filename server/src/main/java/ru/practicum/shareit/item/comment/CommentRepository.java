package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemIdOrderById(long itemId);

    List<Comment> findAllByItemIdInOrderById(List<Long> itemIds);

    @Query(value = "select c from Comment c join fetch c.item it where it.ownerId = :ownerId order by it.id")
    List<Comment> findAllByItemOwnerId(@Param(value = "ownerId") Long ownerId);
}
