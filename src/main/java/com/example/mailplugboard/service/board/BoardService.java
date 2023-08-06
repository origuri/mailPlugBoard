package com.example.mailplugboard.service.board;

import com.example.mailplugboard.model.board.BoardDto;
import com.example.mailplugboard.model.board.BoardListDto;
import com.example.mailplugboard.model.httpResponse.HttpResponseDto;
import com.example.mailplugboard.model.httpResponse.HttpResponseInfo;
import com.example.mailplugboard.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    /*
     * 게시판 전부를 가져오는 메소드
     * 파라미터 : 없음
     * */
    public BoardListDto findBoardList() {
        BoardListDto boardListDto = new BoardListDto();
        List<BoardDto> boardDtos = boardRepository.selectBoardList();
        log.info("boardDto -> {}",boardDtos);
        log.info("boardDto 사이즈 3개여야 함. -> {}",boardDtos.size());
        boardListDto.setValue(boardDtos);
        boardListDto.setCount(boardDtos.size());
        return boardListDto;
    }
    /*
     * 게시판 생성 메소드
     * 파라미터 : boradDto(displayName, boardType)
     * result = 1(성공)
     * */
    public int addBoardByBoardDto(BoardDto boardDto) {

        if(boardDto.getDisplayName() == null || boardDto.getDisplayName().trim().equals("") || boardDto.getBoardType() == null || boardDto.getBoardType().trim().equals("")) {
            return 2;
        }

        int result = boardRepository.insertBoardByBoardDto(boardDto);
        return result;
    }

    /*
     * 게시판 삭제 메소드
     * 파라미터 : boardId
     * result : 1(성공), 3(해당 게시판 없음)
     * */
    public int removeBoardByBoardId(Long boardId) {
        int result = 0;

        BoardDto savedBoardDto = boardRepository.selectBoardByBoardId(boardId);
        if(savedBoardDto == null){
            return 3;
        }

        result = boardRepository.deleteBoardByBoardId(boardId);
        return result;

    }
    /*
     * 게시판 수정 메소드
     * 파라미터 : boardDto(boardId, displayName, boardType, boardState)
     * result : 1(성공), 2(파라미터 오류), 3(해당 게시판 없음)
     * */
    public int modifyBoardByBoardId(BoardDto boardDto) {
        if(boardDto.getDisplayName() == null || boardDto.getDisplayName().trim().equals("")
                || boardDto.getBoardType() == null || boardDto.getBoardType().trim().equals("")
                || boardDto.getBoardStatus() == null || boardDto.getBoardStatus().trim().equals("")){
            return 2;
        }
        BoardDto savedBoardDto = boardRepository.selectBoardByBoardId(boardDto.getBoardId());
        if(savedBoardDto == null){
            return 3;
        }

        return boardRepository.updateBoardByBoardId(boardDto);

    }
}
