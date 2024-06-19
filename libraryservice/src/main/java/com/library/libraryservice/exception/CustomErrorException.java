package com.library.libraryservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public record CustomErrorException (
    @JsonProperty("code")
    String code,
    @JsonProperty("reason")
    String reason,
    @JsonProperty("message")
    String message
){}
