package com.routine.domain.b_circle.repository;


import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.e_board.model.Category;
import com.routine.domain.e_board.model.DetailCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CircleRepository extends JpaRepository<Circle, Long> {

    @Query("SELECT c FROM Circle c " +
            "WHERE c.isOpened = true " +
            "AND (:category IS NULL OR c.category = :category) " +
            "AND (:detailCategory IS NULL OR c.detailCategory = :detailCategory) " +
            "AND (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(c.tags) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Circle> searchCircles(
            @Param("category") Category category,
            @Param("detailCategory") DetailCategory detailCategory, // ← enum 타입으로 수정
            @Param("keyword") String keyword
    );


}
