package com.example.bibou.repository;

import java.util.List;

import com.example.bibou.dto.Todo;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper {
    //1件挿入
    int insert(Todo todo);

    //全件検索
    List<Todo> selectAll();

    //1件検索
    Todo selectByTodoId(int todo_id);

    //ユーザーをキーに検索
    List<Todo> selectByUserId(int user_id);

    //1件更新
    int update(Todo todo);

    //1件削除
    int delete(int todo_id);

}
