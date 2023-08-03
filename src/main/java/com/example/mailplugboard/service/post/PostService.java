package com.example.mailplugboard.service.post;

import com.example.mailplugboard.model.post.PostDto;
import com.example.mailplugboard.model.post.PostListDto;
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
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /*
     * 파라미터 : boardId
     * 해당 게시판의 게시글을 전부 가져오는 메소드
     * */
    public PostListDto findPostListByBoardId(Long boardId) {
        PostListDto postListDto = new PostListDto();
        List<PostDto> postDtos = postRepository.selectPostListByBoardId(boardId);
        postListDto.setValues(postDtos);
        postListDto.setCount(postDtos.size());
        return postListDto;
    }


    /*
     * 파라미터 : boardId, postId
     * 게시글의 상세 내역 조회
     * */
    public PostDto findPostByBoardIdAndPostId(Long boardId, Long postId) {
        Map<String, Long> BoardIdAndPostId = new HashMap<>();
        BoardIdAndPostId.put("boardId", boardId);
        BoardIdAndPostId.put("postId", postId);
        PostDto postDto = postRepository.selectPostByBoradIdAndPostId(BoardIdAndPostId);
        return postDto;
    }
}
