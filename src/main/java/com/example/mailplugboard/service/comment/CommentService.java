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
     * selectCommentDbPasswordByCommentDto =>
     * 내가 입력한 비밀번호와 db의 비밀번하 일치하는 지 확인하는 메소드
     * 파라미터 : commentDto(boardId, postId, commentId)
     * */
    private boolean checkDbPassword(CommentDto commentDto){
        String rawPassword = commentDto.getPassword();
        String dbPassword = commentRepository.selectCommentDbPasswordByCommentDto(commentDto);
        log.info("modifyCommentByCommentDto 비번 확인 -> {}",rawPassword.equals(dbPassword));

        return rawPassword.equals(dbPassword);
    }

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
     * 파라미터 : commentDto(boardId, postId, displayName, password, contents)
     *
     * updatePlusPostCountsByCommentDto
     * => 댓글 등록 성공 시 post에 commentCount를 올려주는 메소드
     * */
    public int addCommentByCommentDto(CommentDto commentDto) {
        if(commentDto.getBoardId() == null || commentDto.getPostId() == null){
            throw new NullPointerException("boardId나 postId가 null 입니다.");
        }
        int plusCommentsCount = 0;
        int result = commentRepository.insertCommentByCommentDto(commentDto);

        if(result > 0){
            plusCommentsCount = postRepository.updatePlusPostCommentsCountByCommentDto(commentDto);
        }

        if(result > 0 && plusCommentsCount > 0){
            return result;
        }else {
            return 0;
        }
    }

    /*
     * 특정 댓글을 수정하는 메소드
     * 파라미터 : CommentDto(boardId, postId, commentId, content, password)
     * */
    public int modifyCommentByCommentDto(CommentDto commentDto) {
        if(commentDto.getBoardId() == null || commentDto.getPostId() == null || commentDto.getCommentId() == null) {
            throw new NullPointerException("boardId나 postId, commentId가 null 입니다.");
        }
        int result = 0;

        if(checkDbPassword(commentDto)){
            result = commentRepository.updateCommentByCommentDto(commentDto);
            return result;
        } else {
            return 2;
        }

    }


    /*
     * 댓글 삭제 메소드
     * 파라미터 CommentDto(boardId, postId, commentId, password)
     *
     * updateMinusPostCommentsCountByCommentDto
     * => 댓글 삭제 시 post의 commentsCount를 감소시킴.
     * 파라미터 CommentDto(boardId, postId)
     * */
    public int removeCommentByCommentDto(CommentDto commentDto) {
        if(commentDto.getBoardId() == null || commentDto.getPostId() == null || commentDto.getCommentId() == null) {
            throw new NullPointerException("boardId나 postId, commentId가 null 입니다.");
        }
        int result = 0;
        int minusCommentCount = 0;

        // db 비번 체크
        if(checkDbPassword(commentDto)){
            result = commentRepository.deleteCommentByCommentDto(commentDto);
        }else {
            // 비번 오류
            return 2;
        }

        // commentsCount 감소
        if(result > 0){
            minusCommentCount = postRepository.updateMinusPostCommentsCountByCommentDto(commentDto);
        }

        log.info("result, deleteCommentCount => {}, {}",result,minusCommentCount);

        if(result == 1 && minusCommentCount == 1){
            return result;
        }else{
            return 0;
        }
    }
}
