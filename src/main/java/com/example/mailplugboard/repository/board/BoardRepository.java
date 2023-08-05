package com.example.mailplugboard.repository.board;

import com.example.mailplugboard.model.board.BoardDto;

import java.util.List;

public interface BoardRepository {
    /*
     * 게시판 전부를 가져오는 메소드
     * 파라미터 : 없음
     * */
    List<BoardDto> selectBoardList();
    /*
     * 게시판 생성 메소드
     * 파라미터 : boradDto(displayName, boardType)
     * result = 1(성공)
     * */
    int insertBoardByBoardDto(BoardDto boardDto);
    /*
     * 게시판 삭제 메소드
     * 파라미터 : boardId
     * result : 1(성공)
     * */
    int deleteBoardByBoardId(Long boardId);
    /*
     * 게시판 수정 메소드
     * 파라미터 : boardDto(boardId, displayName, boardType, boardState)
     * result : 1(성공)
     * */
    int updateBoardByBoardId(BoardDto boardDto);

    /*
     * 해당 게시글 쓸 때 게시판 있는지 확인 하는 메소드
     * 파라미터 : boardId
     * */
    BoardDto selectBoardByBoardId(Long boardId);
}
