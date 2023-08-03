package com.example.mailplugboard.controller.board;

import com.example.mailplugboard.model.board.BoardDto;
import com.example.mailplugboard.model.board.BoardListDto;

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

  /*  @GetMapping("/")
    public String home(){
        return "home";
    }*/
    
    // 게시판 전부를 가져오는 메소드
    @GetMapping("")
    @ResponseBody
    public ResponseEntity boardList(){
        BoardListDto boardListDto = boardService.findBoardList();
        log.info("컨트롤러 boardList boardListDto -> {}",boardListDto);
        return new ResponseEntity<>(boardListDto, HttpStatus.OK);
    }

    // 게시판 생성 form으로 가는 메소드
    @GetMapping("/write")
    public String boardWriteForm(){
        return "board/boardWriteForm";
    }

    // 게시판 생성 메소드
    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<String> boardAddByBoardDto(@RequestBody BoardDto boardDto){
        System.out.println("머나옴?-> "+boardDto.getDisplayName());
        int result = 0;
        result = boardService.addBoardByBoardDto(boardDto);
        if(result > 0){
            return new ResponseEntity<>("등록 완료!",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("등록 실패", HttpStatus.BAD_REQUEST);
        }
    }

    // 게시판 삭제 메소드
    @DeleteMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<String> boardRemoveByBoardId(@PathVariable("boardId") Long boardId){
        int result = 0; 
        result = boardService.removeBoardByBoardId(boardId);
        if(result > 0){
            return new ResponseEntity<>("삭제 완료!",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }

    // 게시판 수정 메소드
    @PutMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<String> boardModifyByBoardId(@RequestBody BoardDto boardDto){
        log.info("boardDto 값 잘 들어왔나? -> {}", boardDto);
        int result = boardService.modifyBoardByBoardId(boardDto);
        if(result > 0){
            return new ResponseEntity<>("수정 완료!",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("수정 실패", HttpStatus.BAD_REQUEST);
        }
    }

}
