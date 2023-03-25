package ru.hikari.crackhash.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hikari.crackhash.messages.*;
import ru.hikari.crackhash.repository.HashRepository;

import java.util.UUID;

@Service
public class ManagerService {
    private static final Logger log = LogManager.getLogger(ManagerService.class);
    @Value("${HASH_WORKERS:1}")
    private String workersAmount;

    @Autowired
    private HashRepository hashRepository;

    public CrackResponse callCrackService(CrackRequest crackRequest) {
        var uuid = UUID.randomUUID();
        hashRepository.registerUUID(uuid.toString(), Integer.parseInt(workersAmount));
        for(int i = 0; i < Integer.parseInt(workersAmount); i++) {
            RestTemplate restTemplate = new RestTemplate();

            String resourceUrl
                    = "http://worker:8080/internal/api/worker/hash/crack/task";

            CrackHashManagerRequest request =
                    InternalRequestFactory.forgeRequest(crackRequest, i, Integer.parseInt(workersAmount), uuid);
            HttpEntity<CrackHashManagerRequest> httpEntity =
                    new HttpEntity<>(request);

            var response = restTemplate.postForEntity(resourceUrl, httpEntity, String.class);
            log.info("manager got response: " + response);
        }
        return new CrackResponse(uuid.toString());
    }

    public StatusResponse getHashStatus(String hashId) {
        var res = hashRepository.findStatus(hashId);
        return new StatusResponse(res.getStatus(), res.getData());
    }

    public void callLogUpdate(CrackHashWorkerResponse updateRequest) {
        hashRepository.updateStatus(
                updateRequest.getRequestId(),
                updateRequest.getAnswers().getWords()
        );
    }
}
