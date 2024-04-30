package org.egglog.api.worktype.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.user.repository.jpa.UserQueryRepositoryImpl;

import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTypeService {

    private final WorkTypeJpaRepository workTypeJpaRepository;

    private final UserQueryRepositoryImpl userQueryRepositoryImpl;


}
