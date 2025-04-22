package com.routine.domain.d_routine_commit.controller;


import com.routine.domain.d_routine_commit.dto.CommitDraftForm;
import com.routine.domain.d_routine_commit.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/commit")
public class CommitController {

    private final CommitService commitService;

    @PostMapping("/{routineId}")
    public String handleCommit(@PathVariable Long routineId,
                               @RequestParam("action") String action,
                               @ModelAttribute CommitDraftForm form) {

        Long memberId = 1L;

        if ("save".equals(action)) {
            commitService.saveDraft(memberId, routineId, form);
        } else if ("submit".equals(action)) {
            commitService.submitDraft(memberId, routineId, form);
        }

        return "redirect:/routine/myRoutines";
    }
}
