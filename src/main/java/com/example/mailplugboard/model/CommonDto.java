package com.example.mailplugboard.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonDto {

    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private String displayName;
}
