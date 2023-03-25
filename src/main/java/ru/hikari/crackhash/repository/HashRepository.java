package ru.hikari.crackhash.repository;

import jakarta.annotation.PostConstruct;

import java.util.*;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.hikari.crackhash.entity.HashStatus;

@Component
public class HashRepository {
    public static final Integer DEFAULT_WORKERS_AMOUNT = 1;
    private static final Map<String, HashStatus> statuses = new HashMap<>();

    @PostConstruct
    public void initData() {
        registerUUID("7u634twgci2345f", DEFAULT_WORKERS_AMOUNT);
        registerUUID("asv5423f2q",DEFAULT_WORKERS_AMOUNT);
        registerUUID("1234",DEFAULT_WORKERS_AMOUNT);
    }

    public HashStatus findStatus(String hash) {
        Assert.notNull(hash, "The hash must not be null");
        var res = statuses.get(hash);
        if(res == null){
            return new HashStatus("NOT_PRESENT", Collections.emptyList(), 0, 0);
        }
        return res;
    }

    public String registerUUID(String hash, Integer toDo){
        statuses.put(hash, new HashStatus("IN_PROGRESS", new ArrayList<>(), toDo, 0));
        return hash;
    }

    public void updateStatus(String hash, List<String> data){
        var status = statuses.get(hash);
        status.setDone(status.getDone() + 1);
        if(status.getToDo().equals(status.getDone())){
            status.setStatus("DONE");
        }
        status.getData().addAll(data);
    }
}