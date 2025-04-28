package com.routine.domain.e_board.repository;


import com.routine.domain.e_board.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByBoardIdOrderByCreatedAtAsc(Long boardId);
}
