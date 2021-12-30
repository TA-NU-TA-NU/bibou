package com.example.bibou.service;

import java.util.List;

import com.example.bibou.dto.Todo;

public interface TodoService {
    Todo register(Todo todo);

    List<Todo> retrieve();

    Todo retrieveFromTodoId(Integer todo_id);

    List<Todo> retrieveFromUserId(Integer user_id);

    Todo update(Todo todo);

    Integer delete(Integer todo_id);
}
