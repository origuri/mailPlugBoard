package com.example.mailplugboard.controller.board;

import com.example.mailplugboard.model.board.BoardDto;
import com.example.mailplugboard.model.board.BoardListDto;

import com.example.mailplugboard.model.httpResponse.HttpResponseDto;
import com.example.mailplugboard.model.httpResponse.HttpResponseInfo;
import com.example.mailplugboard.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;


    @ExceptionHandler()
    

    /*
    * 게시판 전부를 가져오는 메소드
    * 파라미터 : 없음
    * */
    @GetMapping("")
    public ResponseEntity boardList(){
        BoardListDto boardListDto = boardService.findBoardList();
        log.info("컨트롤러 boardList boardListDto -> {}",boardListDto);
        if(boardListDto.getCount() == 0){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage(), boardListDto), HttpStatus.OK);

    }

    /*
    * 게시판 생성 메소드
    * 파라미터 : boradDto(displayName, boardType)
    * result = 1(성공)
    * */
    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity boardAddByBoardDto(@RequestBody BoardDto boardDto){

        if(boardDto.getDisplayName() == null || boardDto.getBoardType() == null){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.BAD_REQUEST.getStatusCode(), HttpResponseInfo.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
        }

        int result = 0;

        result = boardService.addBoardByBoardDto(boardDto);
        if(result == 1){
            return new ResponseEntity<>(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new HttpResponseDto(HttpResponseInfo.INTERNAL_SERVER_ERROR.getStatusCode(), HttpResponseInfo.INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    * 게시판 삭제 메소드
    * 파라미터 : boardId
    * result : 1(성공)
    * */
    @DeleteMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity boardRemoveByBoardId(@PathVariable("boardId") Long boardId){
        int result = 0;
        result = boardService.removeBoardByBoardId(boardId);
        if(result == 1){
            return new ResponseEntity<>(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /*
    * 게시판 수정 메소드
    * 파라미터 : boardDto(boardId, displayName, boardType, boardState)
    * result : 1(성공)
    * */
    @PutMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity boardModifyByBoardId(@PathVariable("boardId") Long boardId ,
                                                       @RequestBody BoardDto boardDto){
        boardDto.setBoardId(boardId);
        if(boardDto.getBoardId() == null || boardDto.getDisplayName() == null || boardDto.getBoardType() == null || boardDto.getBoardState() == null){
            return new ResponseEntity(new HttpResponseDto(HttpResponseInfo.BAD_REQUEST.getStatusCode(), HttpResponseInfo.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
        }

        log.info("boardDto 값 잘 들어왔나? -> {}", boardDto);
        int result = boardService.modifyBoardByBoardId(boardDto);
        if(result == 1){
            return new ResponseEntity<>(new HttpResponseDto(HttpResponseInfo.OK.getStatusCode(), HttpResponseInfo.OK.getMessage()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new HttpResponseDto(HttpResponseInfo.NOT_FOUND.getStatusCode(), HttpResponseInfo.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
