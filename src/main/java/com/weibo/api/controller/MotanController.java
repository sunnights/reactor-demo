package com.weibo.api.controller;

import com.weibo.api.service.MotanDemoServiceExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author sunnights
 */
@RestController
public class MotanController {
    MotanDemoServiceExtension demoService;

    @PostConstruct
    public void init() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:motan_demo_client.xml"});
        demoService = (MotanDemoServiceExtension) ctx.getBean("motanDemoExtensionReferer");
    }

    @RequestMapping("/motan")
    public ResponseEntity<String> hello(@RequestParam("latency") long latency) {
        try {
            TimeUnit.MILLISECONDS.sleep(latency);
            return ResponseEntity.ok("ok");
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("unavailable");
        }
    }

    @RequestMapping("/motan2")
    public Mono<Boolean> hello2(@RequestParam("latency") long latency) {
        return demoService.sleepReactor(latency);
    }
}
