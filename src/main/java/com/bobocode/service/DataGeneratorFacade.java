package com.bobocode.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

@Service
@RequiredArgsConstructor
public class DataGeneratorFacade {
    private final DataGeneratorService dataGeneratorService;
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public void generateData() {
        ForkJoinTask<?> userAndProfiles = forkJoinPool.submit(dataGeneratorService::createUsersAndProfiles);
        ForkJoinTask<?> boards = forkJoinPool.submit(dataGeneratorService::createBoards);
        userAndProfiles.join();
        boards.join();
        forkJoinPool.submit(dataGeneratorService::createUserBoardRecords);
        ForkJoinTask<?> tickets = forkJoinPool.submit(dataGeneratorService::createTickets);
        tickets.join();
        forkJoinPool.submit(dataGeneratorService::createComments);
    }


}
