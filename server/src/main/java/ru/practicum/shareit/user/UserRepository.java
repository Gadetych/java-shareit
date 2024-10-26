package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT EXISTS(SELECT id FROM users WHERE email = ?1)", nativeQuery = true)
    boolean existsByUserEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    void updateUserName(@Param("id") Long id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    void updateUserEmail(@Param("id") Long id, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name, u.email = :email WHERE u.id = :id")
    void updateUserNameAndEmail(@Param("id") Long id, @Param("name") String name, @Param("email") String email);
}
