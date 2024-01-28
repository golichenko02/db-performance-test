package com.bobocode.controller;

import com.bobocode.entity.Ticket;
import com.bobocode.entity.User;
import com.bobocode.service.DataGeneratorFacade;
import com.bobocode.service.DataGeneratorService;
import com.bobocode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/tickets")
    public List<Ticket> getTicketsByUserId(@PathVariable Long userId) {
        return userService.getTicketsByUserId(userId);
    }
}
