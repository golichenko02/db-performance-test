package com.bobocode.service;

import com.bobocode.entity.Ticket;
import com.bobocode.entity.User;
import com.bobocode.repository.TicketRepository;
import com.bobocode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }


    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findTicketsByUserId(userId);
    }

}
