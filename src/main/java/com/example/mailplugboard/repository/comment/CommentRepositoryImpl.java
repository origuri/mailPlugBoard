package com.example.mailplugboard.repository.comment;

import com.example.mailplugboard.model.CommonDto;
import com.example.mailplugboard.model.comment.CommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CommentRepositoryImpl implements CommentRepository{
    private final SqlSession session;

    /*
     * 특정 게시글에 달린 댓글 리스트 가져오는 메소드
     * 파라미터 : boardId, postId
     * */
    @Override
    public List<CommentDto> selectCommentListByBoardIdAndPostId(Map<String, Long> boardIdAndPostId) {
        List<CommentDto> commentDtos = null;
        try {
            commentDtos = session.selectList("selectCommentListByBoardIdAndPostId", boardIdAndPostId);
            log.info("레파지토리 selectCommentListByBoardIdAndPostId commentDtos => {}",commentDtos);
        }catch (Exception e){
            log.error("레파지토리 selectCommentListByBoardIdAndPostId 에러 => {}",e.getMessage());
        }
        return commentDtos;
    }

    /*
     * 댓글 상세 조회
     * 파라미터 : boardId, postId, commentId
     * */
    @Override
    public CommonDto selectCommentByBoardIdAndPostIdAndCommentId(Map<String, Long> boardIdAndPostIdAndCommentId) {
        CommentDto commentDto = null;
        try {
            commentDto = session.selectOne("selectCommentByBoardIdAndPostIdAndCommentId", boardIdAndPostIdAndCommentId);
            log.info("레파지토리 selectCommentByBoardIdAndPostIdAndCommentId commentDto -> {}",commentDto);
        }catch (Exception e){
            log.error("레파지토리 selectCommentByBoardIdAndPostIdAndCommentId 에러 -> {}",e.getMessage());
        }
        return commentDto;
    }

    /*
     * 특정 게시글에 댓글 등록 메소드
     * 파라미터 : boardId, postId, commentDto(displayName, contents)
     * */
    @Override
    public int insertCommentByCommentDto(CommentDto commentDto) {
        int result = 0;
        try {
            result = session.insert("insertCommentByCommentDto", commentDto);
            log.info("레파지토리 insertCommentByCommentDto result -> {}",result);
        }catch (Exception e){
            log.error("레파지토리 insertCommentByCommentDto 에러 -> {}",e.getMessage());
        }
        return result;
    }

    /*
     * 내가 입력한 비밀번호와 db의 비밀번하 일치하는 지 확인하는 메소드
     * 파라미터 : commentDto(boardId, postId, commentId)
     * */
    @Override
    public String selectCommentDbPasswordByCommentDto(CommentDto commentDto) {
        String dbPassword = null;
        try {
            dbPassword = session.selectOne("selectCommentDbPasswordByCommentDto", commentDto);
            log.info("레파지토리 selectCommentDbPasswordByCommentDto dbPassword -> {}",dbPassword);
        }catch (Exception e){
            log.info("레파지토리 selectCommentDbPasswordByCommentDto 에러 -> {}",e.getMessage());
        }
        return dbPassword;
    }

    /*
     * 특정 댓글을 수정하는 메소드
     * 파라미터 : CommentDto(boardId, postId, commentId, content)
     * */
    @Override
    public int updateCommentByCommentDto(CommentDto commentDto) {
        int result = 0;
        try {
            result = session.update("updateCommentByCommentDto", commentDto);
            log.info("레파지토리 updateCommentByCommentDto result -> {}",result);
        }catch (Exception e){
            log.error("레파지토리 updateCommentByCommentDto 에러 -> {}",e.getMessage());
        }

        return result;
    }

    /*
     * 댓글 삭제 메소드
     * 파라미터 CommentDto(boardId, postId, commentId, password)
     * */
    @Override
    public int deleteCommentByCommentDto(CommentDto commentDto) {
        int result = 0;
        try {
            result = session.delete("deleteCommentByCommentDto", commentDto);
            log.info("레파지토리 deleteCommentByCommentDto result -> {}",result);
        }catch (Exception e){
            log.info("레파지토리 deleteCommentByCommentDto 에러 -> {}",e.getMessage());
        }
        return result;
    }
}
