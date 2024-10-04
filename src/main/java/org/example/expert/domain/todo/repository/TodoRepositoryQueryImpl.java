package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

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
                                .and(modifiedDateRange(startDate, endDate))
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

    private BooleanExpression modifiedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
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

    @Override
    public Page<TodoSearchResponse> findAllOrderByCreatedAtDesc(Pageable pageable, String title, LocalDateTime startCreatedAt, LocalDateTime endCreatedAt, String nickname) {
        List<TodoSearchResponse> results = q
                .select(
                        new QTodoSearchResponse(
                                todo.title,
                                manager.countDistinct(),
                                comment.countDistinct()
                        )
                )
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .leftJoin(todo.comments, comment)
                .where(
                        titleLike(title)
                                .and(nicknameLike(nickname))
                                .and(createdDateRange(startCreatedAt, endCreatedAt))
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
    private BooleanExpression titleLike(String title) {
        return title != null ? todo.title.like("%" + title + "%") : todo.title.isNotNull();
    }
    private BooleanExpression nicknameLike(String nickname) {
        return nickname != null ? user.nickname.like("%" + nickname + "%") : user.nickname.isNotNull();
    }
    private BooleanExpression createdDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return todo.createdAt.isNotNull();
        } else if (endDate == null) {
            return todo.createdAt.gt(startDate);
        } else if (startDate == null) {
            return todo.createdAt.lt(endDate);
        } else {
            return todo.createdAt.between(startDate, endDate);
        }
    }
}
