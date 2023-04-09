package ru.hikari.crackhash.controllers;
import org.springframework.context.annotation.Profile;
import ru.hikari.crackhash.messages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hikari.crackhash.services.ManagerService;


@RestController
@Profile("manager")
public class ManagerController {
    private static final Logger log = LogManager.getLogger(ManagerController.class);

    @Autowired
    ManagerService managerService;

    @PostMapping(value = "/api/hash/crack", consumes = "application/json")
    public CrackResponse crackHash(
            @RequestBody CrackRequest crackRequest) {
        log.info("Received crack request with hash: "+crackRequest.toString());
        return managerService.callCrackService(crackRequest);
    }

    @GetMapping(value = "/api/hash/status")
    public StatusResponse getStatus(
            @RequestParam(name = "requestId") String hashId) {
        return managerService.getHashStatus(hashId);
    }
}