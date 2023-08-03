package com.example.mailplugboard.repository.post;


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
     * 파라미터 : postDto(boardId, title, displayName, contents)
     * */
    int insertPostByPostDto(PostDto postDto);

    /*
     * 게시글 수정 메소드
     * 파라미터 : postDto (boardId, postId, title, displayName, contents)
     * */
    int updatePostByPostDto(PostDto postDto);

    /*
     * 게시글 삭제 메소드
     * 파라미터 : boardId, postId
     * */
    int deletePostByBoardIdAndPostId(Map<String, Long> boardIdAndPostId);
}
