package com.example.mailplugboard.model.board;

import com.example.mailplugboard.model.Writer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDto extends Writer {

    private Long boardId;
    private String boardType;
    private String boardState;
}
