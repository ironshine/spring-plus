package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.modifiedAt < :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByEndDateOrderByModifiedAtDesc(Pageable pageable, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.modifiedAt > :startDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByStartDateOrderByModifiedAtDesc(Pageable pageable, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.modifiedAt BETWEEN :startDate AND :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByBetweenStartDateAndEndDateOrderByModifiedAtDesc(Pageable pageable, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.weather LIKE %:weather% ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable, @Param("weather") String weather);


    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.weather LIKE %:weather% AND t.modifiedAt < :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByEndDateOrderByModifiedAtDesc(Pageable pageable, @Param("weather") String weather, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.weather LIKE %:weather% AND t.modifiedAt > :startDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByStartDateOrderByModifiedAtDesc(Pageable pageable, @Param("weather") String weather, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.weather LIKE %:weather% AND t.modifiedAt BETWEEN :startDate AND :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByBetweenStartDateAndEndDateOrderByModifiedAtDesc(Pageable pageable, @Param("weather") String weather, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
