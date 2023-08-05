package com.example.mailplugboard.repository.board;

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

    /*
     * 게시판 전부를 가져오는 메소드
     * 파라미터 : 없음
     * */
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

    /*
     * 게시판 생성 메소드
     * 파라미터 : boradDto(displayName, boardType)
     * */
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

    /*
     * 게시판 삭제 메소드
     * 파라미터 : boardId
     * */
    @Override
    public int deleteBoardByBoardId(Long boardId) {
        int result = 0;
        try {
            result = session.delete("deleteBoardByBoardId", boardId);
        } catch (Exception e){
            log.error("레파지토리 deleteBoardByBoardId 에러 -> {}",e.getMessage());
        }
        return result;
    }
    /*
     * 게시판 수정 메소드
     * 파라미터 : boardDto(boardId, displayName, boardType, boardState)
     * */
    @Override
    public int updateBoardByBoardId(BoardDto boardDto) {
        int result = 0;
        try {
            result = session.update("updateBoardByBoardId", boardDto);
        } catch (Exception e){
            log.error("레파지토리 updateBoardByBoardId 에러 -> {}",e.getMessage());
        }
        return result;
    }

    /*
     * 해당 게시글 쓸 때 게시판 있는지 확인 하는 메소드
     * 파라미터 : boardId
     * */
    @Override
    public BoardDto selectBoardByBoardId(Long boardId) {
        BoardDto boardDto = null;
        try {
            boardDto = session.selectOne("selectBoardByBoardId", boardId);
            log.info("레파지토리 selectBoardByBoardId boardDto => {}",boardDto);
        }catch (Exception e){
            log.error("레파지토리 selectBoardByBoardId 에러 => {}",e.getMessage());

        }
        return boardDto;
    }
}
