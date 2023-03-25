package ru.hikari.crackhash.repository;

import ru.hikari.crackhash.entity.HashStatus;

import java.util.List;

public interface CustomHashRepository {

    void registerHash(String uuid, Integer toDo);

    void commitHashUpdate(HashStatus status);

}
