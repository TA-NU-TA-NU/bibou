package com.example.bibou.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Todo {
    
    @NotNull
    private int todo_id;

    @NotNull
    private int user_id;

    @NotNull
    private String title;

    private String contents;

    @NotNull
    private Timestamp last_update;

}
