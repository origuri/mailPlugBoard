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
     * 파라미터 : boardId
     * 해당 게시판의 게시글을 전부 가져오는 메소드
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
     * 파라미터 : map (boardId, postId)
     * 게시글의 상세 내역 조회
     * */
    @Override
    public PostDto selectPostByBoradIdAndPostId(Map<String, Long> boardIdAndPostId) {
        System.out.println("map 파리미터확인 => "+boardIdAndPostId.get("boardId")+"//"+boardIdAndPostId.get("postId"));
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
}
