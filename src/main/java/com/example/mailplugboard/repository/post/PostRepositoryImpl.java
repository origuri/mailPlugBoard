package com.example.mailplugboard.repository.post;

import com.example.mailplugboard.model.post.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
