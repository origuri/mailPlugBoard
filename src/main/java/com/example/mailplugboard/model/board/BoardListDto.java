package com.example.mailplugboard.model.board;

import lombok.Data;

import java.util.List;

@Data
public class BoardListDto {
    private List<BoardDto> value;
    private int count;

}
