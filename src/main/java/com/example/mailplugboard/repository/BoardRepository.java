package com.example.mailplugboard.repository;

import com.example.mailplugboard.model.board.BoardDto;

import java.util.List;

public interface BoardRepository {
    // 게시판 전체를 가져오는 메소드
    List<BoardDto> selectBoardList();

    int insertBoardByBoardDto(BoardDto boardDto);
}
