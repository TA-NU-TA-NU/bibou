package com.example.bibou.controller;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.bibou.dto.Todo;
import com.example.bibou.service.TodoService;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;

@RunWith(SpringRunner.class)
public class TodoControllerTest {
    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoRestController todoRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("CREATE TEST…観点:新しいtodoの登録の確認と返り値の確認.")
    @Test
    public void testPost() {
        //postに渡すtodoの作成
        Todo todo = new Todo();
        todo.setUser_id(1);
        todo.setTitle("test");
        todo.setContents("this is test");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));

        Mockito.when(todoService.register(todo)).thenReturn(todo);

        Errors errors = mock(Errors.class);
        Todo res = todoRestController.post(todo, errors);

        assertEquals(todo, res);
    }

    @DisplayName("CREATE TEST…観点:フィールドに以上がある場合にエラーがかえること.")
    @Test
    public void testPostAbnormal() {
        //postに渡すtodoの作成
        Todo todo = new Todo();
        //userid is not enough
        todo.setTitle("test");
        todo.setContents("this is test");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));

        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(true);

        assertThrows(RuntimeException.class, () -> todoRestController.post(todo, errors));
    }

    @DisplayName("READ TEST…観点:userIdから情報をレコードを取得できる事.")
    @Test
    public void testGetByUserId(){

        Todo todo2 = new Todo();
        Todo todo3 = new Todo();
        Todo todo4 = new Todo();
        
        todo2.setUser_id(1);
        todo2.setTitle("aaaa");
        todo2.setContents("kore test");
        todo2.setLast_update(new Timestamp(System.currentTimeMillis()));

        todo3.setUser_id(4);
        todo3.setTitle("bbb");
        todo3.setContents("tanuko");
        todo3.setLast_update(new Timestamp(System.currentTimeMillis()));
        
        todo4.setUser_id(1);
        todo4.setTitle("nenn");
        todo4.setContents("nennennenn");
        todo4.setLast_update(new Timestamp(System.currentTimeMillis()));

        List<Todo> todos = new ArrayList<>();
        todos.add(todo2);
        todos.add(todo3);
        todos.add(todo4);

        when(todoService.retrieveFromUserId(anyInt())).thenReturn(todos);

        List<Todo> todosRes = todoRestController.getByUserId();

        assertEquals(todos, todosRes);
        assertEquals(3, todosRes.size());
        verify(todoService,times(1)).retrieveFromUserId(eq(1));
    }

    @DisplayName("READ TEST…観点:todoIdからレコードを取得できること.")
    @Test
    public void testGetByTodoId(){

        Todo todo = new Todo();
        todo.setTodo_id(1);
        todo.setUser_id(1);
        todo.setTitle("aaaa");
        todo.setContents("kore test");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));
        
        when(todoService.retrieveFromTodoId(todo.getTodo_id())).thenReturn(todo);

        Todo todoRes = todoRestController.getByTodoId(1);

        assertEquals(todo, todoRes);
        verify(todoService,times(1)).retrieveFromTodoId(todo.getTodo_id());
    }

    @DisplayName("UPDATE TEST…観点:todoを更新できること.")
    @Test
    public void testPutTodo(){

        Todo todo = new Todo();
        todo.setTodo_id(1);
        todo.setUser_id(1);
        todo.setTitle("aaaa");
        todo.setContents("kore test");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));
        
        when(todoService.update(todo)).thenReturn(todo);

        Errors errors = mock(Errors.class);

        Todo todoRes = todoRestController.putTodo(todo, errors);

        assertEquals(todo, todoRes);
    }

    @DisplayName("UPDATE TEST…観点:フィールドが異常な場合にエラーがかえること.")
    @Test
    public void testPutTodoAbnormal() {
        //postに渡すtodoの作成
        Todo todo = new Todo();
        //userid is not enough
        todo.setTitle("test");
        todo.setContents("this is test");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));

        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(true);

        assertThrows(RuntimeException.class, () -> todoRestController.putTodo(todo, errors));
    }

    @DisplayName("DELETE TEST…観点:todoが削除できる事.")
    @Test
    public void testDeleteTodo(){
        int id = 1;
                
        when(todoService.delete(id)).thenReturn(id);

        int res = todoRestController.deleteTodo(id);

        assertEquals(id, res);
        verify(todoService,times(1)).delete(id);
    }


}
