package com.bobocode.controller;

import com.bobocode.service.DataGeneratorFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/generate")
@RequiredArgsConstructor
public class DataGeneratorController {
    private final AtomicInteger generationCount = new AtomicInteger();
    private final DataGeneratorFacade dataGeneratorFacade;


    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void generateUsers() {
        if (generationCount.getAndIncrement() >= 1) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        dataGeneratorFacade.generateData();
    }
}
