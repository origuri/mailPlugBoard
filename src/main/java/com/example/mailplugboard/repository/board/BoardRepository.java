package com.example.mailplugboard.repository.board;

import com.example.mailplugboard.model.board.BoardDto;

import java.util.List;

public interface BoardRepository {
    // 게시판 전체를 가져오는 메소드
    List<BoardDto> selectBoardList();
    // 게시판 등록 메소드
    int insertBoardByBoardDto(BoardDto boardDto);
    // 게시판 삭제 메소드
    int deleteBoardByBoardId(Long boardId);
    // 게시판 수정 메소드
    int updateBoardByBoardId(BoardDto boardDto);
}
