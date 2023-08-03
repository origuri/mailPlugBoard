package com.example.mailplugboard.controller.comment;

import com.example.mailplugboard.model.CommonDto;
import com.example.mailplugboard.model.comment.CommentDto;
import com.example.mailplugboard.model.comment.CommentListDto;
import com.example.mailplugboard.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("board/{boardId}/post/{postId}")
public class CommentController {
    private final CommentService commentService;

    /*
    * 특정 게시글에 달린 댓글 리스트 가져오는 메소드
    * 파라미터 : boardId, postId
    * */
    @GetMapping("/comments")
    public ResponseEntity commentsListByBoardIdAndPostId(@PathVariable("boardId") Long boardId,
                                                         @PathVariable("postId") Long postId){
        CommentListDto commentListDto = commentService.findCommentListByBoardIdAndPostId(boardId, postId);
        if(commentListDto != null){
            return new ResponseEntity(commentListDto, HttpStatus.OK);
        }else{
            return new ResponseEntity("댓글 목록 조회 실패", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 댓글 상세 조회
    * 파라미터 : boardId, postId, commentId
    * */
    @GetMapping("/comments/{commentId}")
    public ResponseEntity commentDetailByBoardIdAndPostIdAndCommentId(@PathVariable("boardId")   Long boardId,
                                                                      @PathVariable("postId")    Long postId,
                                                                      @PathVariable("commentId") Long commentId){
        CommonDto commonDto = commentService.findCommentByBoardIdAndPostIdAndCommentId(boardId,postId,commentId);
        if(commonDto != null){
            return new ResponseEntity(commonDto, HttpStatus.OK);
        }else{
            return new ResponseEntity("댓글 상세 조회 실패", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 특정 게시글에 댓글 등록 메소드
    * 파라미터 : boardId, postId, commentDto(displayName, contents)
    * */
    @PostMapping("/comments/write")
    @ResponseBody
    public ResponseEntity commentAddByCommentDto(@PathVariable("boardId")   Long boardId,
                                                 @PathVariable("postId")    Long postId,
                                                 @RequestBody CommentDto commentDto){
        commentDto.setBoardId(boardId);
        commentDto.setPostId(postId);
        int result = commentService.addCommentByCommentDto(commentDto);
        if(result > 0){
            return new ResponseEntity("댓글 등록 성공", HttpStatus.OK);
        }else{
            return new ResponseEntity("댓글 등록 실패", HttpStatus.BAD_REQUEST);
        }
    }




}
