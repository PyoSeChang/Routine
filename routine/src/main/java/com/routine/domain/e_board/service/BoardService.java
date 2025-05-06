package com.routine.domain.e_board.service;

import com.routine.domain.e_board.dto.BoardDTO;
import com.routine.domain.e_board.dto.BoardListDTO;
import com.routine.domain.e_board.model.Board;
import com.routine.domain.e_board.model.Category;
import com.routine.domain.e_board.model.DetailCategory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    // 보드 insert
    void insertBoard(BoardDTO board);
    // 보드 update
    void updateBoard(BoardDTO board, Long memberId);
    // 보드 delete
    void deleteBoard(Long boardId);
    // 보드 display
    Page<BoardListDTO> displayBoards(Category category,
                                     DetailCategory detailCategory,
                                     String keyword,
                                     Pageable pageable);
    // 보드 상세보기
    BoardDTO getBoard(Long boardId);

    // 조회수 증가
//    void increaseViewCount(Long boardId, HttpServletRequest request);
}
