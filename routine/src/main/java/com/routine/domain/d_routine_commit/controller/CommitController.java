package com.routine.domain.d_routine_commit.controller;


import com.routine.domain.d_routine_commit.dto.CommitDraftForm;
import com.routine.domain.d_routine_commit.service.CommitService;
import com.routine.domain.d_routine_commit.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/commit")
public class CommitController {

    private final CommitService commitService;
    private final PointService pointService;
    @PostMapping("/{routineId}")
    public ResponseEntity<Void> handleCommit(@PathVariable Long routineId,
                               @RequestParam("action") String action,
                               @ModelAttribute CommitDraftForm form) {

        Long memberId = 1L;
//        System.out.println("바운딩 테스트: "+form.isSkipped());
//        System.out.println("바운딩 테스트: "+form.isSkipped());
//        System.out.println("바운딩 테스트: "+form.isSkipped());

        String isSkippedStr = form.getIsSkippedStr();
        if (isSkippedStr.equals("true")) {
            form.setSkipped(true);
        }
//        System.out.println("바운딩 테스트: "+form.isSkipped());
        if ("save".equals(action)) {
            commitService.saveDraft(memberId, routineId, form);
        } else if ("submit".equals(action)) {
            commitService.submitDraft(memberId, routineId, form);
        }

        pointService.rewardForCircleRoutineCommit(memberId, routineId);

        return ResponseEntity.ok().build();
    }
}
