package ru.hikari.crackhash.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hikari.crackhash.messages.CrackHashManagerRequest;
import ru.hikari.crackhash.services.WorkerService;


@Component
@RabbitListener(queues = "crackhash_queue", id = "worker")
public class RabbitWorkerController {
    private static final Logger log = LogManager.getLogger(RabbitWorkerController.class);

    @Autowired
    WorkerService workerService;

    @RabbitHandler
    public void receiver(CrackHashManagerRequest workRequest) {
        log.info("rabbit-consuming request: " + workRequest.getRequestId());
        log.info("Starting to work on: "+ workRequest.getRequestId());
        workerService.callDoWork(workRequest);
    }
}