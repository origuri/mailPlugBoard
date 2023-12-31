package com.example.mailplugboard.repository.post;


import com.example.mailplugboard.model.comment.CommentDto;
import com.example.mailplugboard.model.post.PostDto;

import java.util.List;
import java.util.Map;

public interface PostRepository {
    /*
     * 해당 게시판의 게시글을 전부 가져오는 메소드
     * 파라미터 : boardId
     * */
    List<PostDto> selectPostListByBoardId(Long boardId);
    /*
     * 게시글의 상세 내역 조회
     * 파라미터 : boardId, postId
     * */
    PostDto selectPostByBoradIdAndPostId(Map<String, Long> boardIdAndPostId);
    /*
     * 게시글 등록 메소드
     * 파라미터 : postDto(boardId, title, displayName, password, contents)
     * */
    int insertPostByPostDto(PostDto postDto);

    /*
     * 게시글 수정 메소드
     * 파라미터 : postDto (boardId, postId, title, displayName, password, contents)
     * */
    int updatePostByPostDto(PostDto postDto);

    /*
     * 게시글 삭제 메소드
     * 파라미터 : postDto(boardId, postId, password)
     * */
    int deletePostByPostDto(PostDto postDto);

    /*
     * 댓글 등록 성공 시에 commentCount를 올려주는 메소드
     * 파라미터 : boardId, postId
     * */
    int updatePlusPostCommentsCountByCommentDto(CommentDto commentDto);

    /*
     * 댓글 삭제 성공 시에 commentCount를 내려주는 메소드
     * 파라미터 : boardId, postId
     * */
    int updateMinusPostCommentsCountByCommentDto(CommentDto commentDto);

    /*
     * selectPostDbPasswordByPostDto =>
     * 내가 입력한 비밀번호와 db의 비밀번하 일치하는 지 확인하는 메소드
     * 파라미터 : postDto(boardId, postId)
     * */
    String selectPostDbPasswordByPostDto(PostDto postDto);
}
