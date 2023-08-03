package com.example.mailplugboard.controller.post;

import com.example.mailplugboard.model.post.PostDto;
import com.example.mailplugboard.model.post.PostListDto;
import com.example.mailplugboard.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("board/{boardId}")
public class PostController {
    private final PostService postService;

    /*
     * 해당 게시판의 게시글을 전부 가져오는 메소드
     * 파라미터 : boardId
     * */
    @GetMapping("/post")
    @ResponseBody
    public ResponseEntity postListByBoardId(@PathVariable("boardId") Long boardId){
        log.info("boardId 잘 넘어오나? -> {}", boardId);
        PostListDto postListDto = postService.findPostListByBoardId(boardId);
        if(postListDto != null){
            return new ResponseEntity(postListDto, HttpStatus.OK);
        }else{
            return new ResponseEntity("게시글 목록 조회 실패", HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * 게시글의 상세 내역 조회
     * 파라미터 : boardId, postId
     * */
    @GetMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity postDetailByBoardIdAndPostId(@PathVariable("boardId") Long boardId,
                                                       @PathVariable("postId") Long postId){
        log.info("boardId, postId 잘 넘오는지 확인 -> {}, {}", boardId, postId);
        PostDto postDto = postService.findPostByBoardIdAndPostId(boardId,postId);
        if(postDto != null){
            return new ResponseEntity(postDto, HttpStatus.OK);
        }else{
            return new ResponseEntity("게시글 상세 조회 실패", HttpStatus.BAD_REQUEST);
        }

    }

    /*
    * 게시글 등록 메소드
    * 파라미터 : postDto(boardId, title, displayName, contents)
    * */
    @PostMapping("/post/write")
    @ResponseBody
    public ResponseEntity postAddByPostDto(@PathVariable("boardId") Long boardId, @RequestBody PostDto postDto){
        postDto.setBoardId(boardId);
        int result = postService.addPostByPostDto(postDto);
        if(result > 0 ){
            return new ResponseEntity("게시글 등록 성공", HttpStatus.OK);
        }else{
            return new ResponseEntity("게시글 등록 실패", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 게시글 수정 메소드
    * 파라미터 : postDto (boardId, postId, title, displayName, contents)
    * */
    @PutMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity postModifyByPostDto(@PathVariable("boardId") Long boardId,
                                              @PathVariable("postId") Long postId,
                                              @RequestBody PostDto postDto){
        log.info("boardId, postId -> {}, {}", boardId, postId);
        postDto.setPostId(postId);
        postDto.setBoardId(boardId);
        int result = postService.modifyPostByPostDto(postDto);
        if(result > 0 ){
            return new ResponseEntity("게시글 수정 성공", HttpStatus.OK);
        }else{
            return new ResponseEntity("게시글 수정 실패", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 게시글 삭제 메소드
    * 파라미터 : boardId, postId
    * */
    @DeleteMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity postRemoveByBoardIdAndPostId(@PathVariable("boardId") Long boardId,
                                                       @PathVariable("postId") Long postId){
        int result = postService.removePostByBoardIdAndPostId(boardId,postId);
        if(result > 0 ){
            return new ResponseEntity("게시글 삭제 성공", HttpStatus.OK);
        }else{
            return new ResponseEntity("게시글 삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }

}
