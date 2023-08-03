package com.example.mailplugboard.model.board;

import com.example.mailplugboard.model.CommonDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDto extends CommonDto {

    private Long boardId;
    private String boardType;
    private String boardState;
}
