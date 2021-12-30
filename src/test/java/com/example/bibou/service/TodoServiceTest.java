package com.example.bibou.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.bibou.dto.Todo;
import com.example.bibou.repository.TodoMapper;
import com.example.bibou.service.impl.TodoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

//TodoServiceのregisterメソッドをテストする
public class TodoServiceTest {
    
    @Mock //モック化するクラスインスタンスの宣言（注入するもの/使われているもの）
    private TodoMapper mapper;

    @InjectMocks //モックを注入するクラスのインスタンスの宣言（注入先）
    private TodoServiceImpl service;

    @BeforeEach //this(mapper)を初期化
    public void setup(){
        MockitoAnnotations.openMocks(this); //初期化する
    }

    @DisplayName("CREATE TEST…観点:todoが登録できる事.")
    @Test //テスト内容
    public void testRegister() {
        //registerに入れるtodoを生成
        Todo todo = new Todo();
        todo.setUser_id(1);
        todo.setTitle("test");
        todo.setContents("this is test");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));

        //when=mock化したインスタンスのメソッドの戻り値(振る舞い)を設定できるメソッド
        //whenの中に戻り値を変更したいメソッドを入れてthenReturn内に設定する値を入れる。
        //Mockito.any(Todo.class)…メソッドに引数をいれる場合
        when(mapper.insert(Mockito.any(Todo.class))).thenReturn(1); //mapper.insertはどんな値を入れても戻り値は1と定義する
        
        Todo actual = service.register(todo);
        
        //入力したtodoと返り値のactualの中身が期待通りであることを確認する
        assertEquals(todo.getUser_id(), actual.getUser_id());
        assertEquals(todo.getTitle(), actual.getTitle());
        assertEquals(todo.getContents(), actual.getContents());
        assertEquals(todo.getLast_update(), actual.getLast_update());

        //verify()でモック化したメソッドが呼ばれているか検証する
        //ここでのmapperはモックインスタンス、Mockito.times(n)で呼ばれた回数の期待値
        //コール回数が1回であることを確認する.
        Mockito.verify(mapper,Mockito.times(1)).insert(todo);
    }

    @DisplayName("READ TEST…観点:todoが全て取得できる事.")
    @Test //テスト内容
    public void testRetrieveAll() {
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
        
        when(mapper.selectAll()).thenReturn(todos);

        List<Todo> actuals = service.retrieve();

        assertEquals(3, actuals.size());
        verify(mapper, times(1)).selectAll();
    }    

    @DisplayName("READ TEST…観点:todoがtodoIdから取得できる事.")
    @Test //テスト内容
    public void testRetrieveFromTodoId() {
        Todo todo = new Todo();
        
        todo.setUser_id(1);
        todo.setTitle("たぬこ");
        todo.setContents("たぬたぬ");

        String str = "2021-12-10 01:01:01.111111";
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        
        try {
            Timestamp timestamp;
            timestamp = new Timestamp(form.parse(str).getTime());
            todo.setLast_update(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        when(mapper.selectByTodoId(anyInt())).thenReturn(todo);
        
        Todo actual = service.retrieveFromTodoId(1);

        assertEquals(todo.getUser_id(),actual.getUser_id());
        assertEquals(todo.getTitle(),actual.getTitle());
        assertEquals(todo.getContents(),actual.getContents());
        assertEquals(todo.getLast_update(),actual.getLast_update());

        verify(mapper, times(1)).selectByTodoId(1);

    }

    @DisplayName("READ TEST…観点:todoがUserIdから取得できる事.")
    @Test //テスト内容
    public void testRetrieveFromUserId() {
        List<Todo> todos = new ArrayList<Todo>();
        Todo todo = new Todo();
        
        todo.setUser_id(1);
        todo.setTitle("たぬこ");
        todo.setContents("たぬたぬ");

        String str = "2021-12-10 01:01:01.111111";
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        
        try {
            Timestamp timestamp;
            timestamp = new Timestamp(form.parse(str).getTime());
            todo.setLast_update(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        todos.add(todo);
        when(mapper.selectByUserId(todo.getUser_id())).thenReturn(todos);
        
        List<Todo> actuals = service.retrieveFromUserId(1);

        assertEquals(todo.getUser_id(),actuals.get(0).getUser_id());
        assertEquals(todo.getTitle(),actuals.get(0).getTitle());
        assertEquals(todo.getContents(),actuals.get(0).getContents());
        assertEquals(todo.getLast_update(),actuals.get(0).getLast_update());

        verify(mapper, times(1)).selectByUserId(todo.getUser_id());
    }

    @DisplayName("UPDATE TEST…観点:todoが更新できる事")
    @Test
    public void testUpdate() {
        Todo todo = new Todo();
        todo.setTodo_id(1);
        todo.setUser_id(4);
        todo.setTitle("ねんこ");
        todo.setContents("ねんねん");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));

        when(mapper.update(any(Todo.class))).thenReturn(1);
        
        Todo actual = service.update(todo);
        
        assertEquals(todo, actual);

        verify(mapper, times(1)).update(todo);
    }

    @DisplayName("DELETE TEST…観点:todoが削除できる事")
    @Test
    public void testDelete() {
        int todo_id = 1;
        when(mapper.delete(todo_id)).thenReturn(1);

        int actual = service.delete(todo_id);

        assertEquals(todo_id,actual);
        verify(mapper, times(1)).delete(todo_id);
    }
}
