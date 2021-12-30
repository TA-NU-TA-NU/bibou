package com.example.bibou.service.impl;

import java.util.List;

import com.example.bibou.dto.Todo;
import com.example.bibou.repository.TodoMapper;
import com.example.bibou.service.TodoService;

import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService{
    private TodoMapper mapper;

    public TodoServiceImpl(TodoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Todo register(Todo todo){
        //中間処理が必要ならここに
        mapper.insert(todo);
        return todo;
    }

    @Override
    public List<Todo> retrieve() {
        return mapper.selectAll();
    }

    @Override
    public Todo retrieveFromTodoId(Integer todo_id) {
        return mapper.selectByTodoId(todo_id);
    }

    @Override
    public List<Todo> retrieveFromUserId(Integer user_id) {
        return mapper.selectByUserId(user_id);
    }

    @Override
    public Todo update(Todo todo) {
        mapper.update(todo);
        return todo;
    }

    @Override
    public Integer delete(Integer todo_id) {
        mapper.delete(todo_id);
        return todo_id;
    }
}
