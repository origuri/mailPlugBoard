package com.example.mailplugboard.controller.post;

import com.example.mailplugboard.model.httpResponse.HttpResponseDto;
import com.example.mailplugboard.model.httpResponse.HttpResponseInfo;
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
    @GetMapping("/posts")
    public ResponseEntity postListByBoardId(@PathVariable("boardId") Long boardId){
        log.info("boardId 잘 넘어오나? -> {}", boardId);
        PostListDto postListDto = postService.findPostListByBoardId(boardId);
        if(postListDto.getCount() > 0){
            return new ResponseEntity(postListDto, HttpStatus.OK);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /*
     * 게시글의 상세 내역 조회
     * 파라미터 : boardId, postId
     * */
    @GetMapping("/post/{postId}")
    public ResponseEntity postDetailByBoardIdAndPostId(@PathVariable("boardId") Long boardId,
                                                       @PathVariable("postId") Long postId){
        log.info("boardId, postId 잘 넘오는지 확인 -> {}, {}", boardId, postId);
        PostDto postDto = postService.findPostByBoardIdAndPostId(boardId,postId);
        if(postDto != null){
            return new ResponseEntity(postDto, HttpStatus.OK);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }

    }

    /*
     * 게시글 등록 메소드
     * 파라미터 : postDto(boardId, title, displayName, password, contents)
     * result : 1(정상), 2(파라미터 오류), 3(해당 게시판이 없음)
     * */
    @PostMapping("/post/write")
    @ResponseBody
    public ResponseEntity postAddByPostDto(@PathVariable("boardId") Long boardId, @RequestBody PostDto postDto){
        postDto.setBoardId(boardId);
        log.info(postDto.getPassword());

        int result = postService.addPostByPostDto(postDto);
        if(result == 1 ){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()), HttpStatus.OK);
        }else if(result == 2){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.BAD_REQUEST.getStatusCode(), HttpResponseInfo.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /*
    * 게시글 수정 메소드
    * 파라미터 : postDto (boardId, postId, title, displayName, password, contents)
    * result : 1(정상), 2(파라미터 오류), 3(해당 게시글이 없음)
    * */
    @PutMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity postModifyByPostDto(@PathVariable("boardId") Long boardId,
                                              @PathVariable("postId") Long postId,
                                              @RequestBody PostDto postDto){
        log.info("boardId, postId -> {}, {}", boardId, postId);
        postDto.setPostId(postId);
        postDto.setBoardId(boardId);

        if(postDto.getPassword() == null) {
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.INTERNAL_SERVER_ERROR.getStatusCode(), HttpResponseInfo.INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        int result = postService.modifyPostByPostDto(postDto);
        if(result == 1 ){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()), HttpStatus.OK);
        }else if(result == 2){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.BAD_REQUEST.getStatusCode(), HttpResponseInfo.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /*
    * 게시글 삭제 메소드
    * 파라미터 : postDto(boardId, postId, password)
    * result : 1(정상), 2(비번 오류), 3(해당 게시글이 없음)
    * */
    @DeleteMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity postRemoveByBoardIdAndPostId(@PathVariable("boardId") Long boardId,
                                                       @PathVariable("postId") Long postId,
                                                       @RequestBody PostDto postDto){

        postDto.setBoardId(boardId);
        postDto.setPostId(postId);
        if(postDto.getPassword() == null) {
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.INTERNAL_SERVER_ERROR.getStatusCode(), HttpResponseInfo.INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result = postService.removePostByPostDto(postDto);

        if(result == 1 ){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()), HttpStatus.OK);
        }else if(result == 2){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.BAD_REQUEST.getStatusCode(), HttpResponseInfo.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
