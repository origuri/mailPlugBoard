package com.example.mailplugboard.controller.comment;

import com.example.mailplugboard.model.CommonDto;
import com.example.mailplugboard.model.comment.CommentDto;
import com.example.mailplugboard.model.comment.CommentListDto;
import com.example.mailplugboard.model.httpResponse.HttpResponseDto;
import com.example.mailplugboard.model.httpResponse.HttpResponseInfo;
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
    @GetMapping("/comment")
    @ResponseBody
    public ResponseEntity commentsListByBoardIdAndPostId(@PathVariable("boardId") Long boardId,
                                                         @PathVariable("postId") Long postId){
        CommentListDto commentListDto = commentService.findCommentListByBoardIdAndPostId(boardId, postId);
        if(commentListDto.getCount() > 0){
            return new ResponseEntity(commentListDto, HttpStatus.OK);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /*
    * 댓글 상세 조회
    * 파라미터 : boardId, postId, commentId
    * */
    @GetMapping("/comment/{commentId}")
    @ResponseBody
    public ResponseEntity commentDetailByBoardIdAndPostIdAndCommentId(@PathVariable("boardId")   Long boardId,
                                                                      @PathVariable("postId")    Long postId,
                                                                      @PathVariable("commentId") Long commentId){
        CommonDto commonDto = commentService.findCommentByBoardIdAndPostIdAndCommentId(boardId,postId,commentId);
        if(commonDto != null){
            return new ResponseEntity(commonDto, HttpStatus.OK);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 특정 게시글에 댓글 등록 메소드
    * 파라미터 : commentDto(boardId, postId, displayName, password, contents)
    * result : 1(정상), 2(등록실패), 3(해당 게시글이 없음)
    * */
    @PostMapping("/comment/write")
    @ResponseBody
    public ResponseEntity commentAddByCommentDto(@PathVariable("boardId")   Long boardId,
                                                 @PathVariable("postId")    Long postId,
                                                 @RequestBody CommentDto commentDto){
        if(commentDto.getPassword() == null || commentDto.getPassword().trim().equals("")){
            return new ResponseEntity<>(new HttpResponseDto(HttpResponseInfo.INTERNAL_SERVER_ERROR.getStatusCode(), HttpResponseInfo.INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        commentDto.setBoardId(boardId);
        commentDto.setPostId(postId);
        int result = commentService.addCommentByCommentDto(commentDto);
        if(result == 1){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()), HttpStatus.OK);
        }else if(result == 2){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.BAD_REQUEST.getStatusCode(), HttpResponseInfo.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /*
    * 특정 댓글을 수정하는 메소드
    * 파라미터 : CommentDto(boardId, postId, commentId, content, password)
    * result : 1(정상), 2(파라미터 오류), 3(해당 댓글이 없음)
    * */
    @PutMapping("/comment/{commentId}")
    @ResponseBody
    public ResponseEntity commentModifyByCommentDto(@PathVariable("boardId")   Long boardId,
                                                    @PathVariable("postId")    Long postId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @RequestBody CommentDto commentDto){
        commentDto.setBoardId(boardId);
        commentDto.setPostId(postId);
        commentDto.setCommentId(commentId);

        if(commentDto.getPassword() == null || commentDto.getPassword().trim().equals("")) {
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.INTERNAL_SERVER_ERROR.getStatusCode(), HttpResponseInfo.INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result = commentService.modifyCommentByCommentDto(commentDto);

        if(result == 1){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()), HttpStatus.OK);
        }else if(result == 2){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.BAD_REQUEST.getStatusCode(), HttpResponseInfo.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /*
    * 댓글 삭제 메소드
    * 파라미터 CommentDto(boardId, postId, commentId, password)
    * result : 1(정상), 2(파라미터 오류), 3(해당 댓글이 없음)
    * */
    @DeleteMapping("/comment/{commentId}")
    @ResponseBody
    public ResponseEntity commentRemoveByCommentDto(@PathVariable("boardId")   Long boardId,
                                                    @PathVariable("postId")    Long postId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @RequestBody CommentDto commentDto){
        commentDto.setBoardId(boardId);
        commentDto.setPostId(postId);
        commentDto.setCommentId(commentId);

        if(commentDto.getPassword() == null || commentDto.getPassword().trim().equals("")) {
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.INTERNAL_SERVER_ERROR.getStatusCode(), HttpResponseInfo.INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result = commentService.removeCommentByCommentDto(commentDto);
        if(result == 1){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()), HttpStatus.OK);
        }else if(result == 2){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.BAD_REQUEST.getStatusCode(), HttpResponseInfo.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
