package com.example.mailplugboard.model.post;

import com.example.mailplugboard.model.CommonDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"boardId", "boardDisplayName", "postId", "title", "displayName" ,"contents", "commentsCount", "createdDateTime","updateDateTime"})
public class PostDto extends CommonDto {
    private Long postId;
    private Long boardId;
    private String title;
    private String contents;
    private String password;
    private int commentsCount;

    // 전체 게시판 조회용
    private String boardDisplayName;

}
