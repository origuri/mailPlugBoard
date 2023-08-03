package com.example.mailplugboard.repository.post;

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
     * 파라미터 : postDto(boardId, title, displayName, contents)
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
     * 파라미터 : postDto (boardId, postId, title, displayName, contents)
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
     * 파라미터 : (map) boardId, postId
     * */
    @Override
    public int deletePostByBoardIdAndPostId(Map<String, Long> boardIdAndPostId) {
        int result = 0;
        try{
            result = session.delete("deletePostByBoardIdAndPostId", boardIdAndPostId);
            log.info("레파지토리 deletePostByBoardIdAndPostId result -> {}",result);
        }catch (Exception e){
            log.error("레파지토리 deletePostByBoardIdAndPostId 에러 -> {}", e.getMessage());
        }
        return result;
    }
}
