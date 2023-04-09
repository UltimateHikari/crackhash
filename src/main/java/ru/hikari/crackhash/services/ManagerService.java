package ru.hikari.crackhash.services;

import jakarta.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.amqp.support.converter.MarshallingMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hikari.crackhash.config.RabbitConfiguration;
import ru.hikari.crackhash.messages.*;
import ru.hikari.crackhash.repository.CustomHashRepository;
import ru.hikari.crackhash.repository.HashRepository;

import java.util.Collections;
import java.util.UUID;

@Service
@Profile("manager")
public class ManagerService {
    private static final Logger log = LogManager.getLogger(ManagerService.class);
    @Value("${HASH_WORKERS:1}")
    private String workersAmount;

    @Autowired
    private CustomHashRepository customHashRepository;
    @Autowired
    private HashRepository hashRepository;
    //private HashRepositoryOld hashRepository;

    @Autowired
    AmqpTemplate rabbitTemplate;
    @Autowired
    private Queue requestQueue;

    public CrackResponse callCrackService(CrackRequest crackRequest) {
        var uuid = UUID.randomUUID();
        customHashRepository.registerHash(uuid.toString(), Integer.parseInt(workersAmount));
        for(int i = 0; i < Integer.parseInt(workersAmount); i++) {

            CrackHashManagerRequest request =
                    InternalRequestFactory.forgeRequest(crackRequest, i, Integer.parseInt(workersAmount), uuid);

            rabbitTemplate.convertAndSend(requestQueue.getName(), request);
            log.info("sent message to queue " + requestQueue.getName());
        }
        return new CrackResponse(uuid.toString());
    }

    public StatusResponse getHashStatus(String hashId) {
        var res = hashRepository.findStatusByUuid(hashId);
        if (res == null){
            return new StatusResponse("NOT_PRESENT", Collections.emptyList());
        }
        return new StatusResponse(res.getStatus(), res.getData());
    }

    public void callLogUpdate(CrackHashWorkerResponse updateRequest) {
        var res = hashRepository.findStatusByUuid(updateRequest.getRequestId());
        if(res == null){
            log.error(updateRequest);
            return;
        }
        res.setDone(res.getDone() + 1);
        if(res.getToDo().equals(res.getDone())){
            res.setStatus("DONE");
        }
        res.getData().addAll(updateRequest.getAnswers().getWords());
        customHashRepository.commitHashUpdate(
                res
        );
    }
}
