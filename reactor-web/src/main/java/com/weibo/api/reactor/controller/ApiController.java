package com.weibo.api.reactor.controller;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author sunnights
 */
@RestController
public class ApiController {
    public static final String BASE_URL = "http://10.77.9.41:9001";
    public static final String URI = "/hello2?latency=";
    public static final String URL = BASE_URL + URI;
    private RestTemplate restTemplate;
    private Scheduler fixedPool;
    private WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .build();

    @PostConstruct
    public void init() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(1000);
        connectionManager.setMaxTotal(1000);
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().setConnectionManager(connectionManager).build()
        ));
        fixedPool = Schedulers.newParallel("poolWithMaxSize", 400);
    }

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

    @RequestMapping("/hello01")
    public String hello01(@RequestParam("latency") long latency) {
        return restTemplate.getForObject(URL + latency, String.class);
    }

    @RequestMapping("/hello02")
    public Mono<String> hello02(@RequestParam("latency") long latency) {
        return Mono.fromCallable(() -> restTemplate.getForObject(URL + latency, String.class))
                .subscribeOn(Schedulers.elastic());
    }

    @RequestMapping("/hello03")
    public Mono<String> hello03(@RequestParam("latency") long latency) {
        return Mono.fromCallable(() -> restTemplate.getForObject(URL + latency, String.class))
                .subscribeOn(fixedPool);
    }

    @RequestMapping("/hello04")
    public Mono<String> hello04(@RequestParam("latency") long latency) {
        return webClient.get()
                .uri(URI + latency)
                .retrieve()
                .bodyToMono(String.class);
    }

    @RequestMapping("/hello05")
    public String hello05(@RequestParam("latency") long latency) {
        return webClient.get()
                .uri(URI + latency)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @RequestMapping("/hello06")
    public String hello06(@RequestParam("latency") long latency) {
        return Mono.fromCallable(() -> restTemplate.getForObject(URL + latency, String.class))
                .subscribeOn(Schedulers.elastic()).block();
    }
}
