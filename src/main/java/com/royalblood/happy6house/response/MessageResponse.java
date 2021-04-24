package com.royalblood.happy6house.response;


import lombok.Getter;
import lombok.ToString;

@Getter
public class MessageResponse {
    private String message;
    public MessageResponse(String message) {
        this.message = message;
    }
}
