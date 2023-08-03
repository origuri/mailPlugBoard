package com.example.mailplugboard.service.comment;

import com.example.mailplugboard.model.CommonDto;
import com.example.mailplugboard.model.comment.CommentDto;
import com.example.mailplugboard.model.comment.CommentListDto;
import com.example.mailplugboard.repository.comment.CommentRepository;
import com.example.mailplugboard.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /*
     * 특정 게시글에 달린 댓글 리스트 가져오는 메소드
     * 파라미터 : boardId, postId
     * */
    public CommentListDto findCommentListByBoardIdAndPostId(Long boardId, Long postId) {
        if(boardId == null || postId == null){
            throw new NullPointerException("boardId나 postId가 null 입니다.");
        }
        CommentListDto commentListDto = new CommentListDto();

        Map<String, Long> boardIdAndPostId = new HashMap<>();
        boardIdAndPostId.put("boardId", boardId);
        boardIdAndPostId.put("postId", postId);

        List<CommentDto> commentDtos = commentRepository.selectCommentListByBoardIdAndPostId(boardIdAndPostId);
        commentListDto.setValue(commentDtos);
        commentListDto.setCount(commentDtos.size());


        return commentListDto;

    }

    /*
     * 댓글 상세 조회
     * 파라미터 : boardId, postId, commentId
     * */
    public CommonDto findCommentByBoardIdAndPostIdAndCommentId(Long boardId, Long postId, Long commentId) {
        if(boardId == null || postId == null || commentId == null) {
            throw new NullPointerException("boardId나 postId, commentId가 null 입니다.");
        }

        Map<String, Long> boardIdAndPostIdAndCommentId = new HashMap<>();

        boardIdAndPostIdAndCommentId.put("boardId", boardId);
        boardIdAndPostIdAndCommentId.put("postId", postId);
        boardIdAndPostIdAndCommentId.put("commentId", commentId);

        CommonDto commonDto = commentRepository.
                selectCommentByBoardIdAndPostIdAndCommentId(boardIdAndPostIdAndCommentId);

        return commonDto;
    }

    /*
     * 특정 게시글에 댓글 등록 메소드
     * 파라미터 : boardId, postId, commentDto(displayName, contents)
     *
     * updatePostCountsByCommentDto
     * => 댓글 등록 성공 시 post에 commentCount를 올려주는 메소드
     * */
    public int addCommentByCommentDto(CommentDto commentDto) {
        if(commentDto.getBoardId() == null || commentDto.getPostId() == null){
            throw new NullPointerException("boardId나 postId가 null 입니다.");
        }
        int commentsCountPlus = 0;
        int result = commentRepository.insertCommentByCommentDto(commentDto);

        if(result > 0){
            Map<String, Long> boardIdAndPostId = new HashMap<>();
            log.info("댓글 수 업뎃 boardId, postId -> {}, {}",commentDto.getBoardId(),commentDto.getPostId());
            boardIdAndPostId.put("boardId", commentDto.getBoardId());
            boardIdAndPostId.put("postId", commentDto.getPostId());

            commentsCountPlus = postRepository.updatePostCountsByCommentDto(boardIdAndPostId);
        }

        if(result > 0 && commentsCountPlus > 0){
            return result;
        }else {
            return 0;
        }
    }
}
