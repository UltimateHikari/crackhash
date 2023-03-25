package ru.hikari.crackhash.messages;

/*
POST /api/hash/crack
Request body:
{
    "hash":"e2fc714c4727ee9395f324cd2e7f331f",
    "maxLength": 4
}
 */

import lombok.Data;
import lombok.NonNull;

@Data
public class CrackRequest {
    @NonNull private String hash;
    @NonNull private Integer maxLength;
}
