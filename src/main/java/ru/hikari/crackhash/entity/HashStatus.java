package ru.hikari.crackhash.entity;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class HashStatus {
    @NonNull
    private String status;
    @NonNull private List<String> data;
    @NonNull private Integer toDo;
    @NonNull private Integer done;
}
