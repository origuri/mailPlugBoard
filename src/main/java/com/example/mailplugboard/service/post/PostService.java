package com.example.mailplugboard.service.post;

import com.example.mailplugboard.model.board.BoardDto;
import com.example.mailplugboard.model.post.PostDto;
import com.example.mailplugboard.model.post.PostListDto;
import com.example.mailplugboard.repository.board.BoardRepository;
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
    private final BoardRepository boardRepository;

    /*
     * selectPostDbPasswordByPostDto =>
     * 내가 입력한 비밀번호와 db의 비밀번하 일치하는 지 확인하는 메소드
     * 파라미터 : postDto(boardId, postId)
     * */
    private boolean checkDbPassword(PostDto postDto){
        log.info("rawPassword 확인 -> {}",postDto.getPassword());
        String rawPassword = postDto.getPassword();
        String dbPassword = postRepository.selectPostDbPasswordByPostDto(postDto);
        log.info("modifyCommentByCommentDto 비번 확인 -> {}",rawPassword.equals(dbPassword));

        return rawPassword.equals(dbPassword);
    }
    /*
    * 게시글 수정 시 해당 게시글이 존재하는 지 확인하는 메소드
    * 파라미터 : boardId, postId
    * */
    private boolean checkSavedPostDto(Long boardId, Long postId){
        Map<String, Long> BoardIdAndPostId = new HashMap<>();
        BoardIdAndPostId.put("boardId", boardId);
        BoardIdAndPostId.put("postId", postId);
        PostDto savedPostDto = postRepository.selectPostByBoradIdAndPostId(BoardIdAndPostId);
        if(savedPostDto == null){
            return false;
        }else{
            return true;
        }
    }

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
     * 파라미터 : postDto(boardId, title, displayName, password, contents)
     * result : 1(정상), 2(파라미터 오류), 3(해당 게시판이 없음)
     * */
    public int addPostByPostDto(PostDto postDto) {

        if(postDto.getDisplayName() == null || postDto.getDisplayName().trim().equals("")
                || postDto.getTitle() == null || postDto.getTitle().trim().equals("")
                || postDto.getContents() == null || postDto.getContents().trim().equals("")
                || postDto.getPassword() == null || postDto.getPassword().trim().equals("")){
            return 2;
        }

        BoardDto boardDto = boardRepository.selectBoardByBoardId(postDto.getBoardId());
        if(boardDto == null){
            return 3;
        }

        return postRepository.insertPostByPostDto(postDto);
    }

    /*
     * 게시글 수정 메소드
     * 파라미터 : postDto (boardId, postId, title, displayName, password, contents)
     * result : 1(정상), 2(파라미터 오류), 3(해당 게시글이 없음)
     * */
    public int modifyPostByPostDto(PostDto postDto) {

        int result = 0;

        boolean checkSavedPostDto = checkSavedPostDto(postDto.getBoardId(), postDto.getPostId());
        if(!checkSavedPostDto){
            result = 3;
            return result;
        }

        if(checkDbPassword(postDto) &&
                ((postDto.getTitle() != null && !postDto.getTitle().trim().equals(""))&&(postDto.getContents() != null && !postDto.getContents().trim().equals("")))){
            result = postRepository.updatePostByPostDto(postDto);
            return result;
        } else {
            result = 2;
            return result;
        }
    }

    /*
     * 게시글 삭제 메소드
     * 파라미터 : postDto(boardId, postId, password)
     * result : 1(정상), 2(비번 오류), 3(해당 게시글이 없음)
     * */
    public int removePostByPostDto(PostDto postDto) {

        int result = 0;

        boolean checkSavedPostDto = checkSavedPostDto(postDto.getBoardId(), postDto.getPostId());
        if(!checkSavedPostDto){
            result = 3;
            return result;
        }

        if(checkDbPassword(postDto)){
            result = postRepository.deletePostByPostDto(postDto);
            return result;
        } else {
            result = 2;
            return result;
        }
    }
}
