package com.example.mailplugboard.repository.comment;

import com.example.mailplugboard.model.CommonDto;
import com.example.mailplugboard.model.comment.CommentDto;

import java.util.List;
import java.util.Map;

public interface CommentRepository {

    /*
     * 특정 게시글에 달린 댓글 리스트 가져오는 메소드
     * 파라미터 : boardId, postId
     * */
    List<CommentDto> selectCommentListByBoardIdAndPostId(Map<String, Long> boardIdAndPostId);

    /*
     * 댓글 상세 조회
     * 파라미터 : boardId, postId, commentId
     * */
    CommonDto selectCommentByBoardIdAndPostIdAndCommentId(Map<String, Long> boardIdAndPostIdAndCommentId);

    /*
     * 특정 게시글에 댓글 등록 메소드
     * 파라미터 : boardId, postId, commentDto(displayName, contents)
     * */
    int insertCommentByCommentDto(CommentDto commentDto);
}
