package com.example.mailplugboard.model.post;

import lombok.Data;

import java.util.List;

@Data
public class PostListDto {
    private List<PostDto> values;
    private int count;
}
