package ru.hikari.crackhash.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.paukov.combinatorics3.Generator;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hikari.crackhash.messages.CrackHashManagerRequest;
import ru.hikari.crackhash.messages.CrackHashWorkerResponse;
import ru.hikari.crackhash.messages.InternalRequestFactory;

import java.util.ArrayList;
import java.util.List;


@Service
public class WorkerService {
    private static final Logger log = LogManager.getLogger(WorkerService.class);


    @Async
    public void callDoWork(CrackHashManagerRequest workRequest) {
        List<String> results = searchForResults(workRequest);
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        String resourceUrl
                = "http://manager:8080/internal/api/manager/hash/crack/request";

        CrackHashWorkerResponse response = InternalRequestFactory.forgeResponse(workRequest, results);
        HttpEntity<CrackHashWorkerResponse> httpEntity =
                new HttpEntity<>(response);
        var httpResponse = restTemplate.patchForObject(resourceUrl, httpEntity, String.class);
        log.info("worker got response: " + httpResponse);
    }

    /*
     * TODO: in config with many workers:
     *  count maxWork, batchWork = maxWork/partCount
     *  .skip(batchWork*partNumber).limit(batchWork)
     */
    private ArrayList<String> searchForResults(CrackHashManagerRequest workRequest) {
        var list =  new ArrayList<String>();
        for (int i = 1; i <= workRequest.getMaxLength(); i++) {
            Generator.combination(workRequest.getAlphabet()
                            .getSymbols())
                    .multi(i)
                    .stream()
                    .forEach(comb -> {
                                var combString = String.join("", comb);
                                if(DigestUtils.md5Hex(combString).equals(workRequest.getHash())){
                                    list.add(combString);
                                }
                            }
                    );
        }
        return list;
    }
}
