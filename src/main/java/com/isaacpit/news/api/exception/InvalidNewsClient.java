package com.isaacpit.news.api.exception;

import lombok.Getter;

@Getter
public class InvalidNewsClient extends RuntimeException {

    String message;

    public InvalidNewsClient(String message) {
        this.message = message;
    }
}
