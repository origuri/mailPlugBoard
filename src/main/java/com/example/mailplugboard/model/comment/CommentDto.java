package com.example.mailplugboard.model.comment;

import com.example.mailplugboard.model.CommonDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"boardId", "postId", "postTitle", "commentId", "displayName" ,"contents", "createdDateTime","updateDateTime"})
public class CommentDto extends CommonDto {

    private Long commentId;
    private Long boardId;
    private Long postId;
    private String contents;
    private String password;

    // 어떤 post에 달린 댓글인지 확인 용
    private String postTitle;

}
