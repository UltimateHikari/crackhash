package ru.hikari.crackhash.messages;

import lombok.Data;
import lombok.NonNull;

import java.util.List;
/*
Response body:
{
"status":"IN_PROGRESS",
"data": null
}
{
   "status":"READY",
   "data": ["abcd"]
}
 */

@Data
public class StatusResponse {
    @NonNull private String status;
    @NonNull private List<String> data;
}
