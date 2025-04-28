package com.routine.domain.e_board.controller;

import com.routine.domain.e_board.dto.BoardDTO;
import com.routine.domain.e_board.dto.BoardListDTO;
import com.routine.domain.e_board.model.Category;
import com.routine.domain.e_board.model.DetailCategory;
import com.routine.domain.e_board.service.BoardService;
import com.routine.security.model.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록 조회
    /**
     * 게시글 목록 조회 (검색 + 페이징)
     *
     * @param category       대분류(Optional)
     * @param detailCategory 세부분류(Optional)
     * @param keyword        제목 검색어(Optional)
     * @param pageable       Spring Data Pageable (page, size, sort)
     * @return Page<BoardListDTO>
     */
    @GetMapping
    public Page<BoardListDTO> displayBoards(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) DetailCategory detailCategory,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return boardService.displayBoards(category, detailCategory, keyword, pageable);
    }

    // 게시글 등록
    @PostMapping
    public void insertBoard(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody BoardDTO boardDTO
    ) {
        BoardDTO requestDTO = boardDTO.toBuilder()
                .writer(principal.getMember().getId())  // writer 서버에서 세팅
                .build();

        boardService.insertBoard(requestDTO);
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public void updateBoard(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long boardId,
            @RequestBody BoardDTO boardDTO
    ) {
        Long memberId = principal.getMember().getId();

        BoardDTO updatedDTO = boardDTO.toBuilder()
                .boardId(boardId)
                .build();

        boardService.updateBoard(updatedDTO, memberId);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public void deleteBoard(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long boardId
    ) {
        boardService.deleteBoard(boardId);
    }

    // 게시글 상세 보기
    @GetMapping("/{boardId}")
    public BoardDTO getBoard(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }
}
