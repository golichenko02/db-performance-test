package com.bobocode.service;

import com.bobocode.entity.*;
import com.bobocode.repository.*;
import com.google.common.base.Stopwatch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataGeneratorService {
    private static final int FLUSH_COUNT = 10_000;
    private static final int USER_RECORDS_COUNT = 10_000_000;
    private static final int BOARD_RECORDS_COUNT = 5_000_000;
    private static final int USER_BOARD_RECORDS_COUNT = 10_000_000;
    private static final int TICKET_RECORDS_COUNT = 20_000_000;
    private static final int COMMENT_RECORDS_COUNT = 40_000_000;
    private static final String INSERT_USER_BOARD_SQL = "INSERT INTO user_board(user_id, board_id) VALUES(?, ?);";

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final BoardRepository boardRepository;
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final EntityManager entityManager;
    // TODO: fill User table 10m records
    // TODO: fill Profile table 10m records
    // TODO: fill board table 5m records
    // TODO: fill user_board table 10m records
    // TODO: fill ticket table 20m records
    // TODO: fill comment table 40m records

    @Transactional
    public void createUsersAndProfiles() {
        String email = "test-%d@gmail.com";
        String username = "username-test-%d";

        log.info("Started to create user and profiles...");
        Stopwatch start = Stopwatch.createStarted();
        for (int i = 1; i <= USER_RECORDS_COUNT; i++) {
            User user = new User(email.formatted(i), username.formatted(i));
            userRepository.save(user);
            Profile profile = generateProfile(i);
            profile.setUser(user);
            profileRepository.save(profile);
            if (i % FLUSH_COUNT == 0) {
                userRepository.flush();
                profileRepository.flush();
                entityManager.clear();
                log.info("Flushed next portion of Users and Profiles to DB of size {}. Total records processed {}", FLUSH_COUNT, i);
                log.info("Time elapsed for Users and Profiles from start {} ms", start.elapsed(TimeUnit.MILLISECONDS));
            }
        }

        userRepository.flush();
        profileRepository.flush();
        entityManager.clear();
        log.info("Create users and profiles took {} ms", start.elapsed(TimeUnit.MILLISECONDS));
    }

    @Transactional
    public void createBoards() {
        String name = "Board-%d";

        log.info("Started to create boards...");
        Stopwatch start = Stopwatch.createStarted();
        for (int i = 1; i <= BOARD_RECORDS_COUNT; i++) {
            Board board = new Board(name.formatted(i));
            boardRepository.save(board);
            if (i % FLUSH_COUNT == 0) {
                boardRepository.flush();
                entityManager.clear();
                log.info("Flushed next portion of board to DB of size {}. Total records processed {}", FLUSH_COUNT, i);
                log.info("Time elapsed for boards from start {} ms", start.elapsed(TimeUnit.MILLISECONDS));
            }
        }
        boardRepository.flush();
        entityManager.clear();
        log.info("Create boards took {} ms", start.elapsed(TimeUnit.MILLISECONDS));
    }


    @Transactional
    public void createUserBoardRecords() {
        Session session = entityManager.unwrap(Session.class);
        log.info("Started to create user_board...");
        Stopwatch start = Stopwatch.createStarted();
        session.doWork(connection -> {
            long userId = 1L;
            long boardId = 1L;
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_BOARD_SQL);
            for (int i = 1; i <= USER_BOARD_RECORDS_COUNT; i++) {
                preparedStatement.setLong(1, userId);
                preparedStatement.setLong(2, boardId);
                preparedStatement.addBatch();

                userId++;
                if (i % 2 == 0) {
                    boardId++;
                }

                if (i % FLUSH_COUNT == 0) {
                    preparedStatement.executeBatch();
                    log.info("Flushed next portion of user_board to DB of size {}. Total records processed {}", FLUSH_COUNT, i);
                    log.info("Time elapsed for user_board from start {} ms", start.elapsed(TimeUnit.MILLISECONDS));
                }

            }
            preparedStatement.executeBatch();
            log.info("Create User_Boards took {} ms", start.elapsed(TimeUnit.MILLISECONDS));
        });


    }


    @Transactional
    public void createTickets() {
        log.info("Started to create tickets...");
        Stopwatch start = Stopwatch.createStarted();
        for (int i = 1; i <= TICKET_RECORDS_COUNT; i++) {
            Ticket ticket = createTicket(i);
            ticketRepository.save(ticket);
            if (i % FLUSH_COUNT == 0) {
                ticketRepository.flush();
                entityManager.clear();
                log.info("Flushed next portion of tickets to DB of size {}. Total records processed {}", FLUSH_COUNT, i);
                log.info("Time elapsed for ticket from start {} ms", start.elapsed(TimeUnit.MILLISECONDS));
            }
        }
        ticketRepository.flush();
        entityManager.clear();
        log.info("Create tickets took {} ms", start.elapsed(TimeUnit.MILLISECONDS));
    }

    @Transactional
    public void createComments() {
        log.info("Started to create comments...");
        Stopwatch start = Stopwatch.createStarted();
        for (int i = 1; i <= COMMENT_RECORDS_COUNT; i++) {
            Comment comment = createComment(i);
            commentRepository.save(comment);
            if (i % FLUSH_COUNT == 0) {
                ticketRepository.flush();
                entityManager.clear();
                log.info("Flushed next portion of comments to DB of size {}. Total records processed {}", FLUSH_COUNT, i);
                log.info("Time elapsed for comments from start {} ms", start.elapsed(TimeUnit.MILLISECONDS));
            }
        }
        ticketRepository.flush();
        entityManager.clear();
        log.info("Create comments took {} ms", start.elapsed(TimeUnit.MILLISECONDS));
    }

    private Ticket createTicket(int i) {
        String title = "title-%d".formatted(i);
        String description = "description-%d".formatted(i);
        Ticket.TicketStatus status = Ticket.TicketStatus.values()[ThreadLocalRandom.current().nextInt(0, 3)];
        Ticket ticket = new Ticket(title, description, status);
        Long userId = ThreadLocalRandom.current().nextLong(1, USER_RECORDS_COUNT);
        Long boardId = ThreadLocalRandom.current().nextLong(1, BOARD_RECORDS_COUNT);
        User user = userRepository.getReferenceById(userId);
        Board board = boardRepository.getReferenceById(boardId);
        ticket.setUser(user);
        ticket.setBoard(board);
        return ticket;
    }

    private Comment createComment(int i) {
        String body = "body-%d".formatted(i);
        Comment comment = new Comment(body);
        Long userId = ThreadLocalRandom.current().nextLong(1, USER_RECORDS_COUNT);
        Long ticketId = ThreadLocalRandom.current().nextLong(1, TICKET_RECORDS_COUNT);
        User user = userRepository.getReferenceById(userId);
        Ticket ticket = ticketRepository.getReferenceById(ticketId);
        comment.setUser(user);
        comment.setTicket(ticket);
        return comment;
    }

    private static Profile generateProfile(int i) {
        String title = "title-%d";
        String department = "department-%d";
        String pictureUrl = "https://google.com/%d";
        String phoneNumber = String.valueOf(ThreadLocalRandom.current().nextInt(1, 10));
        return new Profile(title.formatted(i), department.formatted(i), phoneNumber, pictureUrl.formatted(i));
    }
}
