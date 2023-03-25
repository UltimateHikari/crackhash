package ru.hikari.crackhash.messages;

/*
{
    "requestId":"730a04e6-4de9-41f9-9d5b-53b88b17afac"
}
 */

import lombok.Data;
import lombok.NonNull;

@Data
public class CrackResponse {
    @NonNull private String requestId;
}
