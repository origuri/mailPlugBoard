package com.example.mailplugboard.model.board;

import com.example.mailplugboard.model.CommonDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"boardId", "displayName","boardType","boardState","createdDateTime","updateDateTime"})
public class BoardDto extends CommonDto {

    private Long boardId;
    private String boardType;
    private String boardStatus;
}
