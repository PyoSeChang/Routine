package com.routine.domain.c_routine.controller;

import com.routine.domain.c_routine.dto.AllRoutinesViewDTO;
import com.routine.domain.c_routine.dto.CommitLogDTO;
import com.routine.domain.c_routine.dto.RoutineCommitRatesResponse;
import com.routine.domain.c_routine.dto.RoutineDTO;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.c_routine.service.RoutineViewService;
import com.routine.domain.c_routine.dto.MessageDTO;
import com.routine.domain.d_routine_commit.service.CommitMessageService;
import com.routine.domain.d_routine_commit.service.CommitService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/routine")
@RequiredArgsConstructor
public class RoutineViewController {

    private final RoutineViewService routineViewService;
    private final RoutineService routineService;
    private final CommitService commitService;
    private final RoutineRepository routineRepository;
    private final CommitMessageService commitMessageService;

    @GetMapping("/week")
    public ResponseEntity<List<AllRoutinesViewDTO>> getWeeklyRoutines(@AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();
        LocalDate today = LocalDate.now();

        routineService.initializeTodayCommitLogs();
        // 전체 주간 루틴 반환
        List<AllRoutinesViewDTO> routines = routineViewService.getWeeklyRoutineView(memberId, today);
        return ResponseEntity.ok(routines);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createRoutine(@RequestBody RoutineDTO dto,
                                              @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("👀 Principal: " + auth.getPrincipal());
        Long memberId = principal.getMember().getId();
        Routine routine = routineService.saveRoutine(dto, memberId);
        return ResponseEntity.ok(routine.getId());
    }

    @GetMapping("/{routineId}")
    public ResponseEntity<RoutineDTO> getRoutine(@PathVariable Long routineId,
                                                 @AuthenticationPrincipal PrincipalDetails principal) {
        Long loginMemberId = principal.getMember().getId();

        Long routineOwnerId = routineRepository.findMemberIdById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴에 연결된 멤버가 없습니다."));

        if (!Objects.equals(loginMemberId, routineOwnerId)) {
            throw new AccessDeniedException("해당 루틴을 조회할 권한이 없습니다.");
        }

        RoutineDTO routineDTO = routineService.getRoutine(routineId);

        return ResponseEntity.ok(routineDTO);
    }




    @GetMapping("/{routineId}/messages")
    public ResponseEntity<List<MessageDTO>> getRoutineMessages(@PathVariable Long routineId) {
        List<MessageDTO> messages = commitMessageService.getMyMessages(routineId);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/{routineId}/rates")
    public ResponseEntity<RoutineCommitRatesResponse> getRoutineCommitRates(@PathVariable Long routineId) {
        RoutineCommitRatesResponse rates = routineViewService.getCommitRates(routineId);
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/{routineId}/commit-dates")
    public List<LocalDate> getCommitDates(
            @PathVariable Long routineId,
             @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Long memberId = principal.getMember().getId();
        return commitService.getCommitDatesByRoutineId(memberId, routineId);
    }


    @GetMapping("/{routineId}/commit")
    public CommitLogDTO getCommitsByRoutineAndDate(
            @PathVariable Long routineId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Long memberId = principal.getMember().getId();
        return commitService.getCommitLogSummary(memberId, routineId, date);
    }

}
