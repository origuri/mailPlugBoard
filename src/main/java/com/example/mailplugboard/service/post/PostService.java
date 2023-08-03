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
     * 해당 게시판의 게시글을 전부 가져오는 메소드
     * 파라미터 : boardId
     * */
    public PostListDto findPostListByBoardId(Long boardId) {
        PostListDto postListDto = new PostListDto();
        List<PostDto> postDtos = postRepository.selectPostListByBoardId(boardId);
        postListDto.setValues(postDtos);
        postListDto.setCount(postDtos.size());
        return postListDto;
    }


    /*
     * 게시글의 상세 내역 조회
     * 파라미터 : boardId, postId
     * */
    public PostDto findPostByBoardIdAndPostId(Long boardId, Long postId) {
        Map<String, Long> BoardIdAndPostId = new HashMap<>();
        BoardIdAndPostId.put("boardId", boardId);
        BoardIdAndPostId.put("postId", postId);
        PostDto postDto = postRepository.selectPostByBoradIdAndPostId(BoardIdAndPostId);
        return postDto;
    }

    /*
     * 게시글 등록 메소드
     * 파라미터 : postDto(boardId, title, displayName, contents)
     * */
    public int addPostByPostDto(PostDto postDto) {
        if(postDto.getDisplayName() == null || postDto.getTitle() == null || postDto.getBoardId() == null || postDto.getContents() == null) {
            throw new NullPointerException("게시물 등록에 실패하였습니다.");
        }
        return postRepository.insertPostByPostDto(postDto);
    }

    /*
     * 게시글 수정 메소드
     * 파라미터 : postDto (boardId, postId, title, displayName, contents)
     * */
    public int modifyPostByPostDto(PostDto postDto) {
        if(postDto.getDisplayName() == null || postDto.getTitle() == null || postDto.getBoardId() == null || postDto.getContents() == null || postDto.getPostId() == null) {
            throw new NullPointerException("게시물 수정에 실패하였습니다.");
        }
        return postRepository.updatePostByPostDto(postDto);
    }

    /*
     * 게시글 삭제 메소드
     * 파라미터 : boardId, postId
     * */
    public int removePostByBoardIdAndPostId(Long boardId, Long postId) {
        if(boardId == null || postId == null){
            throw new NullPointerException("게시물 삭제에 실패하였습니다.");
        }
        Map<String, Long> boardIdAndPostId = new HashMap<>();
        boardIdAndPostId.put("boardId", boardId);
        boardIdAndPostId.put("postId", postId);
        return postRepository.deletePostByBoardIdAndPostId(boardIdAndPostId);
    }
}
