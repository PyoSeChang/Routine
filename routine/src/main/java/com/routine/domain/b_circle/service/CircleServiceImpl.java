package com.routine.domain.b_circle.service;


import com.routine.domain.b_circle.repository.CircleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CircleServiceImpl implements CircleService {

    private final CircleRepository circleRepository;


}
