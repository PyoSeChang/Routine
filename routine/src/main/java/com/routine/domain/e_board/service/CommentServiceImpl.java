package com.routine.domain.e_board.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.e_board.dto.CommentCreateDTO;
import com.routine.domain.e_board.dto.CommentDTO;
import com.routine.domain.e_board.dto.CommentUpdateDTO;
import com.routine.domain.e_board.model.Board;
import com.routine.domain.e_board.model.Comment;
import com.routine.domain.e_board.repository.BoardRepository;
import com.routine.domain.e_board.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // src/main/java/com/routine/domain/e_board/service/impl/CommentServiceImpl.java
    @Override
    @Transactional
    public List<CommentDTO> getComments(Long boardId) {
        // 게시글 존재 확인
        boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다. id=" + boardId));

        // 1) 댓글 전부를 flat 리스트로 조회
        List<CommentDTO> all = commentRepository
                .findAllByBoardIdOrderByCreatedAtAsc(boardId)
                .stream()
                .map(CommentDTO::fromEntity)
                .collect(Collectors.toList());

        // 2) ID → DTO 매핑
        Map<Long, CommentDTO> map = all.stream()
                .collect(Collectors.toMap(CommentDTO::getCommentId, dto -> dto));

        // 3) 계층 구조용 루트 리스트
        List<CommentDTO> roots = new ArrayList<>();

        // 4) parentId로 트리 빌드
        for (CommentDTO dto : all) {
            if (dto.getParentId() == null) {
                roots.add(dto);
            } else {
                CommentDTO parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getReplies().add(dto);
                } else {
                    // parentId가 없거나 잘못된 경우도 루트 취급
                    roots.add(dto);
                }
            }
        }

        return roots;
    }


    @Override
    @Transactional
    public CommentDTO addComment(Long userId, Long boardId, CommentCreateDTO createDTO) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다. id=" + boardId));
        Member writer = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다. id=" + userId));

        Comment comment = Comment.builder()
                .board(board)
                .writer(writer)
                .content(createDTO.getContent())
                .parentId(createDTO.getParentId())
                .isDeleted(false)
                .build();

        Comment saved = commentRepository.save(comment);
        return CommentDTO.fromEntity(saved);
    }

    @Override
    @Transactional
    public CommentDTO updateComment(Long userId, Long boardId, Long commentId, CommentUpdateDTO updateDTO) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다. id=" + commentId));
        // 게시글 일치 확인
        if (!comment.getBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("잘못된 게시글에 대한 댓글입니다.");
        }
        // 작성자 확인
        if (!comment.getWriter().getId().equals(userId)) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        comment.setContent(updateDTO.getContent());
        Comment updated = commentRepository.save(comment);
        return CommentDTO.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long boardId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다. id=" + commentId));
        if (!comment.getBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("잘못된 게시글에 대한 댓글입니다.");
        }
        if (!comment.getWriter().getId().equals(userId)) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }
        // Soft delete
        comment.setDeleted(true);
        commentRepository.save(comment);
    }
}
