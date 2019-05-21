package com.weibo.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author sunnights
 */
@RestController
public class ApiController {

    @RequestMapping("/hello")
    public ResponseEntity<String> hello(@RequestParam("latency") long latency) {
        try {
            TimeUnit.MILLISECONDS.sleep(latency);
            return ResponseEntity.ok("ok");
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("unavailable");
        }
    }

    @RequestMapping("/hello2")
    public Mono<ResponseEntity<String>> hello2(@RequestParam("latency") long latency) {
        return Mono.just(ResponseEntity.ok("ok"))
                .delayElement(Duration.ofMillis(latency));
    }
}
