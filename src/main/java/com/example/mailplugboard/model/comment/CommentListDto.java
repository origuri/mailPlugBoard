package com.example.mailplugboard.model.comment;

import lombok.Data;

import java.util.List;

@Data
public class CommentListDto {
    private List<CommentDto> value;
    private int count;
}
