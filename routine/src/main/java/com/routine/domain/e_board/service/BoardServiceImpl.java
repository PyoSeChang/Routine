package com.routine.domain.e_board.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.e_board.dto.BoardDTO;
import com.routine.domain.e_board.dto.BoardListDTO;
import com.routine.domain.e_board.dto.CommentDTO;
import com.routine.domain.e_board.model.*;
import com.routine.domain.e_board.repository.BoardRepository;
import com.routine.domain.e_board.repository.BoardStatusRepository;
import com.routine.domain.e_board.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardStatusRepository boardStatusRepository;
    private final CommentService commentService;
    @Override
    @Transactional
    public void insertBoard(BoardDTO boardDTO) {
        Member writer = memberRepository.findById(boardDTO.getWriter())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        Board board = Board.builder()
                .writer(writer)
                .title(boardDTO.getTitle())
                .tags(boardDTO.getTags())
                .content(boardDTO.getContent())
                .type(BoardType.valueOf(boardDTO.getBoardType()))
                .category(Category.valueOf(boardDTO.getCategory()))
                .detailCategory(DetailCategory.valueOf(boardDTO.getDetailCategory()))
                .build();

        boardRepository.save(board); // 1. 먼저 Board 저장

        BoardStatus status = BoardStatus.builder()
                .board(board)
                .isDisabled(false)
                .viewCount(0)
                .reportCount(0)
                .build();

        boardStatusRepository.save(status); // 2. 그다음 BoardStatus 저장
    }

    @Override
    @Transactional
    public void updateBoard(BoardDTO boardDTO, Long memberId) {
        Board board = boardRepository.findById(boardDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Long authorId = board.getWriter().getId();
        if (!memberId.equals(authorId)) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setType(BoardType.valueOf(boardDTO.getBoardType()));
        board.setCategory(Category.valueOf(boardDTO.getCategory()));
        board.setDetailCategory(DetailCategory.valueOf(boardDTO.getDetailCategory()));

        boardRepository.save(board);

        BoardStatus status = boardStatusRepository.findById(board.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 상태를 찾을 수 없습니다."));

        status.setUpdatedAt(boardDTO.getUpdatedAt());
        boardStatusRepository.save(status);
    }


    @Override
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public Page<BoardListDTO> displayBoards(
            Category category,
            DetailCategory detailCategory,
            String keyword,
            Pageable pageable
    ) {
        // 동적 검색 조건 빌더
        Specification<Board> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> preds = new ArrayList<>();
            if (category != null) {
                preds.add(cb.equal(root.get("category"), category));
            }
            if (detailCategory != null) {
                preds.add(cb.equal(root.get("detailCategory"), detailCategory));
            }
            if (keyword != null && !keyword.isBlank()) {
                preds.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + keyword.trim().toLowerCase() + "%"
                        )
                );
            }
            return preds.isEmpty()
                    ? cb.conjunction()
                    : cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        // 페이징 포함 조회 후 DTO 변환
        return boardRepository
                .findAll(spec, pageable)
                .map(BoardListDTO::fromEntity);
    }

    @Override
    public BoardDTO getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + boardId)
                );
        BoardStatus status = boardStatusRepository.findById(boardId).orElseThrow();
        List<CommentDTO> comments = commentService.getComments(boardId);
        String nickname = memberRepository.findNicknameById(board.getWriter().getId());
        return BoardDTO.fromEntity(board, status, comments, nickname);
    }
}

