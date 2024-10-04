package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery {

    private final JPAQueryFactory q;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                q
                        .select(todo)
                        .from(todo)
                        .leftJoin(todo.user).fetchJoin()
                        .where(
                                todo.id.eq(todoId)
                        ).fetchOne());
    }

    @Override
    public Page<TodoResponse> findAllOrderByModifiedAtDesc(Pageable pageable, String weather, LocalDateTime startDate, LocalDateTime endDate) {
        List<Todo> results = q
                .select(todo)
                .from(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(
                        weatherLike(weather)
                                .and(dateRange(startDate, endDate))
                )
                .orderBy(todo.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<TodoResponse> content = results.stream()
                .map(todo ->
                        new TodoResponse(
                                todo.getId(),
                                todo.getTitle(),
                                todo.getContents(),
                                todo.getWeather(),
                                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                                todo.getCreatedAt(),
                                todo.getModifiedAt()
                        )
                ).toList();
        return new PageImpl<>(content, pageable, results.size());
    }

    private BooleanExpression weatherLike(String weather) {
        return weather != null ? todo.weather.like("%" + weather + "%") : todo.weather.isNotNull();
    }

    private BooleanExpression dateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return todo.modifiedAt.isNotNull();
        } else if (endDate == null) {
            return todo.modifiedAt.gt(startDate);
        } else if (startDate == null) {
            return todo.modifiedAt.lt(endDate);
        } else {
            return todo.modifiedAt.between(startDate, endDate);
        }
    }
}
