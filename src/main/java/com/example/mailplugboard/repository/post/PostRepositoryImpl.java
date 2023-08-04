package com.example.mailplugboard.repository.post;

import com.example.mailplugboard.model.comment.CommentDto;
import com.example.mailplugboard.model.post.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepositoryImpl implements PostRepository{

    private final SqlSession session;

    /*
     * 해당 게시판의 게시글을 전부 가져오는 메소드
     * 파라미터 : boardId
     * */
    @Override
    public List<PostDto> selectPostListByBoardId(Long boardId) {
        List<PostDto> postDtos = null;
        try{
            postDtos = session.selectList("selectPostListByBoardId", boardId);
            log.info("레파지토리 selectPostListByBoardId => {}",postDtos);
        } catch (Exception e){
            log.error("레파지토리 selectPostListByBoardId 에러 => {}",e.getMessage());
        }
        return postDtos;
    }

    /*
     * 게시글의 상세 내역 조회
     * 파라미터 : boardId, postId
     * */
    @Override
    public PostDto selectPostByBoradIdAndPostId(Map<String, Long> boardIdAndPostId) {
        PostDto postDto = null;
        try {
            postDto = session.selectOne("selectPostByBoardIdAndPostId", boardIdAndPostId);
            log.info("레파지토리 selectPostByBoardIdAndPostId postDto -> {}",postDto);
        }catch (Exception e){
            log.info("레파지토리 selectPostByBoardIdAndPostId postDto -> {}",postDto);
            log.error("레파지토리 selectPostByBoardIdAndPostId 에러 -> {}", e.getMessage());
        }
        return postDto;
    }
    /*
     * 게시글 등록 메소드
     * 파라미터 : postDto(boardId, title, displayName, password, contents)
     * */
    @Override
    public int insertPostByPostDto(PostDto postDto) {
        int result = 0;
        try {
            result = session.insert("insertPostByPostDto", postDto);
            log.info("레파지토리 insertPostByPostDto result -> {}",result);
        }catch (Exception e){
            log.error("레파지토리 insertPostByPostDto result -> {}",e.getMessage());
        }
        return result;
    }

    /*
     * 게시글 수정 메소드
     * 파라미터 : postDto (boardId, postId, title, displayName, password, contents)
     * */
    @Override
    public int updatePostByPostDto(PostDto postDto) {
        int result = 0;
        try {
            result = session.update("updatePostByPostDto", postDto);
            log.info("레파지토리 updatePostByPostDto result => {}",result);
        }catch (Exception e){
            log.error("레파지토리 updatePostByPostDto 에러 => {}",e.getMessage());
        }
        return result;
    }

    /*
     * 게시글 삭제 메소드
     * 파라미터 : postDto(boardId, postId, password)
     * */
    @Override
    public int deletePostByPostDto(PostDto postDto) {
        int result = 0;
        try{
            result = session.delete("deletePostByPostDto", postDto);
            log.info("레파지토리 deletePostByPostDto result -> {}",result);
        }catch (Exception e){
            log.error("레파지토리 deletePostByPostDto 에러 -> {}", e.getMessage());
        }
        return result;
    }

    /*
     * 댓글 등록 성공 시에 commentCount를 올려주는 메소드
     * 파라미터 : boardId, postId
     * */
    @Override
    public int updatePlusPostCommentsCountByCommentDto(CommentDto commentDto) {
        int result = 0;
        try{
            result = session.update("updatePlusPostCommentsCountByCommentDto", commentDto);
            log.info("레파지토리 updatePostCountsByCommentDto result -> {}",result);
        }catch (Exception e){
            log.error("레파지토리 updatePostCountsByCommentDto 에러 -> {}", e.getMessage());

        }
        return result;
    }

    /*
     * 댓글 삭제 성공 시에 commentCount를 내려주는 메소드
     * 파라미터 : boardId, postId
     * */
    @Override
    public int updateMinusPostCommentsCountByCommentDto(CommentDto commentDto) {
        int result = 0;
        try {
            result = session.update("updateMinusPostCommentsCountByCommentDto", commentDto);
            log.info("레파지토리 updateMinusPostCommentsCountByCommentDto result -> {}",result);
        }catch (Exception e){
            log.error("레파지토리 updateMinusPostCommentsCountByCommentDto 에러 -> {}", e.getMessage());

        }
        return result;
    }
    /*
     * selectPostDbPasswordByPostDto =>
     * 내가 입력한 비밀번호와 db의 비밀번하 일치하는 지 확인하는 메소드
     * 파라미터 : postDto(boardId, postId)
     * */
    @Override
    public String selectPostDbPasswordByPostDto(PostDto postDto) {
        String dbPassword = null;
        try {
            dbPassword = session.selectOne("selectPostDbPasswordByPostDto", postDto);
            log.info("레파지토리 selectPostDbPasswordByPostDto dbPassword -> {}",dbPassword);
        }catch (Exception e){
            log.info("레파지토리 selectPostDbPasswordByPostDto 에러 -> {}",e.getMessage());
        }
        return dbPassword;
    }
}
