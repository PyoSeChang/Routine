package com.routine.domain.e_board.repository;

import com.routine.domain.e_board.model.Board;
import com.routine.domain.e_board.model.Category;
import com.routine.domain.e_board.model.DetailCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>,
                                         JpaSpecificationExecutor<Board> {

    List<Board> findAllByCategory(Category category);

    List<Board> findAllByDetailCategory(DetailCategory detailCategory);
}
