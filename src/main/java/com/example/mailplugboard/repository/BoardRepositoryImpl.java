package com.example.mailplugboard.repository;

import com.example.mailplugboard.model.board.BoardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {
    private final SqlSession session;

    // 게시판 조회 메소드
    @Override
    public List<BoardDto> selectBoardList() {
        List<BoardDto> boardDtos = null;
        try{
            boardDtos = session.selectList("selectBoardList");
            log.info("레파지토리 boardDtos => {}",boardDtos);
        }catch (Exception e){
            log.error("selectBoardList 에러 -> {}", e.getMessage());
        }
        return boardDtos;
    }

    // 게시판 생성 메소드
    @Override
    public int insertBoardByBoardDto(BoardDto boardDto) {
        int result = 0;
        try {
            result = session.insert("insertBoardByBoardDto", boardDto);
            log.info("레파지토리 insertBoardByBoardDto result -> {}",result);
        }catch (Exception e){
            log.error("레파지토리 insertBoardByBoardDto 에러 -> {}", e.getMessage());
        }
        return result;
    }
}
