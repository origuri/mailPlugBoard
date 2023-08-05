package com.example.mailplugboard.service.comment;

import com.example.mailplugboard.model.CommonDto;
import com.example.mailplugboard.model.comment.CommentDto;
import com.example.mailplugboard.model.comment.CommentListDto;
import com.example.mailplugboard.model.post.PostDto;
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
     * 댓글 수정, 삭제 시 해당 댓글이 존재하는 지 확인하는 메소드
     * 파라미터 : boardId, postId, commentId
     * */
    private boolean checkSavedCommentDto(Long boardId, Long postId, Long commentId){
        Map<String, Long> boardIdAndPostIdAndCommentId = new HashMap<>();

        boardIdAndPostIdAndCommentId.put("boardId", boardId);
        boardIdAndPostIdAndCommentId.put("postId", postId);
        boardIdAndPostIdAndCommentId.put("commentId", commentId);

        CommonDto savedCommonDto = commentRepository.
                selectCommentByBoardIdAndPostIdAndCommentId(boardIdAndPostIdAndCommentId);
        if(savedCommonDto == null){
            return false;
        }else{
            return true;
        }
    }

    /*
     * 특정 게시글에 달린 댓글 리스트 가져오는 메소드
     * 파라미터 : boardId, postId
     * */
    public CommentListDto findCommentListByBoardIdAndPostId(Long boardId, Long postId) {

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
            throw new NullPointerException("boardId, postId, commentId 중 null값이 있습니다.");
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
     * result : 1(정상), 2(파라미터 오류), 3(해당 게시글이 없음)
     *
     * updatePlusPostCountsByCommentDto
     * => 댓글 등록 성공 시 post에 commentCount를 올려주는 메소드
     * */
    public int addCommentByCommentDto(CommentDto commentDto) {

        if(commentDto.getDisplayName() == null
                || commentDto.getDisplayName().trim().equals("")
                || commentDto.getContents() == null
                || commentDto.getContents().trim().equals("")){
            return 2;
        }



        Map<String, Long> BoardIdAndPostId = new HashMap<>();
        BoardIdAndPostId.put("boardId", commentDto.getBoardId());
        BoardIdAndPostId.put("postId", commentDto.getPostId());
        PostDto postDto = postRepository.selectPostByBoradIdAndPostId(BoardIdAndPostId);
        if(postDto == null){
            return 3;
        }

        int plusCommentsCount = 0;
        int result = commentRepository.insertCommentByCommentDto(commentDto);

        if(result == 1){
            plusCommentsCount = postRepository.updatePlusPostCommentsCountByCommentDto(commentDto);
        } else {
            return 2;
        }

        if(result == 1 && plusCommentsCount == 1){
            return result;
        }else {
            return 2;
        }
    }

    /*
     * 특정 댓글을 수정하는 메소드
     * 파라미터 : CommentDto(boardId, postId, commentId, content, password)
     * result : 1(정상), 2(파라미터 오류), 3(해당 댓글이 없음)
     * */
    public int modifyCommentByCommentDto(CommentDto commentDto) {

        int result = 0;

        // 먼저 수정하는 댓글이 존재하는지 확인.
        boolean checkSavedCommentDto = checkSavedCommentDto(commentDto.getBoardId(),commentDto.getPostId(),commentDto.getCommentId());
        if(!checkSavedCommentDto){
            result = 3;
            return result;
        }

        if(checkDbPassword(commentDto) && (commentDto.getContents() != null && !commentDto.getContents().trim().equals(""))){
            result = commentRepository.updateCommentByCommentDto(commentDto);
            return result;
        } else {
            result = 2;
            return result;
        }

    }


    /*
     * 댓글 삭제 메소드
     * 파라미터 CommentDto(boardId, postId, commentId, password)
     * result : 1(정상), 2(파라미터 오류), 3(해당 댓글이 없음)
     *
     * updateMinusPostCommentsCountByCommentDto
     * => 댓글 삭제 시 post의 commentsCount를 감소시킴.
     * 파라미터 CommentDto(boardId, postId)
     * */
    public int removeCommentByCommentDto(CommentDto commentDto) {

        int result = 0;
        int minusCommentCount = 0;
        // 먼저 해당 댓글이 있는지 확인 후
        boolean checkSavedCommentDto = checkSavedCommentDto(commentDto.getBoardId(),commentDto.getPostId(),commentDto.getCommentId());
        if(!checkSavedCommentDto){
            result = 3;
            return result;
        }

        // db 비번 체크
        if(checkDbPassword(commentDto)){
            result = commentRepository.deleteCommentByCommentDto(commentDto);
        }else {
            // 비번 오류
            return 2;
        }

        // commentsCount 감소
        if(result == 1){
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
