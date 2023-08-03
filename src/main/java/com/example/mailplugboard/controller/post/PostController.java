package com.example.mailplugboard.controller.post;

import com.example.mailplugboard.model.post.PostListDto;
import com.example.mailplugboard.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("board/{boardId}")
public class PostController {
    private final PostService postService;

    /*
     * 파라미터 : boardId
     * 해당 게시판의 게시글을 전부 가져오는 메소드
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



}
