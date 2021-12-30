package com.example.bibou.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import com.example.bibou.config.DbConfig;
import com.example.bibou.dto.Todo;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest //テスト起動
@Transactional //テストの前後でテーブルの中身が変わらないようにする
@Import(DbConfig.class)
public class TodoMapperTest{
    @Autowired
    private TodoMapper mapper; //DIされたテスト対象

    @Autowired
    private DataSource ds; //DIされたデータソース設定情報を保持するBean
    private IDatabaseConnection dbconn; //データベースとのコネクション
    private IDataSet inputCsvDataSet; //データセットを持たせるもの

    @BeforeEach //初期化処理
    public void setup() throws Exception{
        this.dbconn = new DatabaseConnection(this.ds.getConnection()); //データソースからデータベース接続を取得
        this.inputCsvDataSet = new CsvDataSet(new File("src/test/resources/java/com/example/bibou/repository/")); 
        DatabaseOperation.CLEAN_INSERT.execute(dbconn, inputCsvDataSet);
        //DatabaseOperation.INSERT.execute(dbconn, inputCsvDataSet); //CSVを基に、データベースをクリーンアップ＆インサート
    }

    @AfterEach
    public void teardown()throws Exception {
        this.dbconn.close(); //後始末
    }

    @DisplayName("INSERT TEST…観点:todoをインサートできる事.")
    @Test
    public void testInsert(){
        Todo todo = new Todo();
        todo.setUser_id(1);
        todo.setTitle("test");
        todo.setContents("this is test");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));

        assertEquals(1, mapper.insert(todo));
    }

    @DisplayName("SELECT TEST…観点:全てのtodoを取得できる事.")
    @Test
    public void testSelectAll() {
        List<Todo> actuals = mapper.selectAll();
        assertEquals(2, actuals.size());
    }

    @DisplayName("SELECT TEST…観点:todoIdからtodoを取得できる事.")
    @Test
    public void testSelect() {
        List<Todo> actuals = mapper.selectAll();
        Todo actual = mapper.selectByTodoId(actuals.get(0).getTodo_id());
        //assertNotNull(actual);
        assertEquals(actuals.get(0).getTodo_id(), actual.getTodo_id());
        assertEquals(1,actual.getUser_id());
        assertEquals("たぬこ",actual.getTitle());
        assertEquals("たぬたぬ",actual.getContents());
        assertEquals("2021-12-10 01:01:01.111111",actual.getLast_update().toString());
    }

    @DisplayName("SELECT TEST…観点:UserIdからtodoを取得できる事")
    @Test
    public void testSelectByUserId() {
        List<Todo> actuals = mapper.selectByUserId(1);
        assertEquals(1, actuals.size());
        assertEquals("たぬこ", actuals.get(0).getTitle());
        assertEquals("たぬたぬ", actuals.get(0).getContents());
        assertEquals("2021-12-10 01:01:01.111111", actuals.get(0).getLast_update().toString());
    }

    @DisplayName("UPDATE TEST…観点:todoが更新できる事")
    @Test
    public void testUpdate() {
        List<Todo> actuals = mapper.selectByUserId(1);
        Todo todo = new Todo();
        todo.setTodo_id(actuals.get(0).getTodo_id());
        todo.setUser_id(4);
        todo.setTitle("ねんこ");
        todo.setContents("ねんねん");
        todo.setLast_update(new Timestamp(System.currentTimeMillis()));
        
        assertEquals(1, mapper.update(todo));
    }

    @DisplayName("UPDATE TEST…観点:todoが削除できる事")
    @Test
    public void testDelete(){
        assertEquals(1, mapper.delete(mapper.selectByUserId(1).get(0).getTodo_id()));
    }


}