package com.example.bibou.controller;

import java.util.List;

import com.example.bibou.dto.Todo;
import com.example.bibou.service.TodoService;

import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/usr")
public class TodoRestController {

    private TodoService todoServise;

    public TodoRestController(TodoService todoService){
        this.todoServise = todoService;
    }

    @PostMapping
    public Todo post(@Validated @RequestBody Todo todo,Errors errors) {
        if (errors.hasErrors()) {
            throw new RuntimeException((Throwable)errors);
        }
        return todoServise.register(todo);
    }

    @GetMapping()
    public List<Todo> getByUserId(){
        //事前にユーザーIDを取得する
        int user_id = 1; //仮
        return todoServise.retrieveFromUserId(user_id);
    }

    @GetMapping("/{todo_id}")
    public Todo getByTodoId(@PathVariable Integer todo_id){
        return todoServise.retrieveFromTodoId(todo_id);
    }

    @PutMapping
    public Todo putTodo(@Validated @RequestBody Todo todo, Errors errors) {
        if (errors.hasErrors()) {
            throw new RuntimeException((Throwable) errors);
        }
        return todoServise.update(todo);
    }

    @DeleteMapping("/{todo_id}")
    public Integer deleteTodo(@PathVariable Integer todo_id) {
        return todoServise.delete(todo_id);
    }

}
