package com.routine.domain.e_board.repository;

import com.routine.domain.e_board.model.Board;
import com.routine.domain.e_board.model.BoardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardStatusRepository extends JpaRepository<BoardStatus, Long> {

}
