package ru.hikari.crackhash.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hikari.crackhash.messages.CrackHashManagerRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import ru.hikari.crackhash.services.WorkerService;


@RestController
public class WorkerController {
    private static final Logger log = LogManager.getLogger(WorkerController.class);

    @Autowired
    WorkerService workerService;

    @PostMapping(value = "/internal/api/worker/hash/crack/task", produces = "application/xml")
    public void doWork(
            @RequestBody CrackHashManagerRequest workRequest) {
        log.info("Starting to work on: "+workRequest.getRequestId());
        workerService.callDoWork(workRequest);
    }

}