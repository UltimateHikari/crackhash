package ru.hikari.crackhash.repository;

import com.mongodb.client.result.UpdateResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.hikari.crackhash.entity.HashStatus;
import ru.hikari.crackhash.services.ManagerService;

import java.util.ArrayList;
import java.util.List;


@Component
public class CustomHashRepositoryImpl implements CustomHashRepository {
    private static final Logger log = LogManager.getLogger(CustomHashRepository.class);

    @Autowired
    MongoTemplate mongoTemplate;

    public void registerHash(String uuid, Integer toDo){

        var result = mongoTemplate.insert(
                new HashStatus(uuid, "IN_PROGRESS", new ArrayList<>(), toDo, 0)
        );

        log.info(result.getStatus() + " document registered..");
    }

    public void commitHashUpdate(HashStatus status) {
        Query query = new Query(Criteria.where("uuid").is(status.getUuid()));
        Update update = new Update();
        update.set("status", status.getStatus());
        update.set("data", status.getData());
        update.set("done", status.getDone());

        UpdateResult result = mongoTemplate.updateFirst(query, update, HashStatus.class);

        log.info(result.getModifiedCount() + " document(s) updated..");
    }

}
