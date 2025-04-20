package com.routine.domain.d_routine_commit.service;



import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommitServiceImpl implements CommitService {

    private final CommitLogRepository commitRepository;


}
